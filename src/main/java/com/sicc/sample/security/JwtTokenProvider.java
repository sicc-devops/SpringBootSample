package com.sicc.sample.security;

import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.sicc.sample.vo.LoginVO;
import com.sicc.sample.vo.UserVO;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenProvider {

  @Value("${security.jwt.token.secret-key:tech}")
  public String secretKey = "tech";

  @Value("${security.jwt.token.expire-length:1800000}")
  public long validityInMilliseconds = 100000000; 

  @Autowired
  private JwtUserDetailsService myUserDetails;
  
  private static Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
  
  @PostConstruct
  protected void init() {
    secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
  }

  public String createToken(UserVO loginVO, List<JwtRole> roles) {
    
	Claims claims = Jwts.claims().setSubject(loginVO.getUserId());
	claims.put("auth", roles.stream().map(s -> new SimpleGrantedAuthority(s.getAuthority())).filter(Objects::nonNull).collect(Collectors.toList()));

    Date now = new Date();
    Date validity;
    
    validityInMilliseconds = 680000000;
	validity = new Date(now.getTime() + validityInMilliseconds);
    
    return Jwts.builder()//
        //.setClaims(claims)//
        .setIssuedAt(now)//
        .setExpiration(validity)//
        .claim("userid", loginVO.getUserId())
        .claim("username", loginVO.getUserName())
        .signWith(SignatureAlgorithm.HS256, secretKey)//
        .compact();
  }

  public Authentication getAuthentication(String token) {
    UserDetails userDetails = myUserDetails.loadUserByUsername(getUserId(token));
    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
  }

  public String getUsername(String token) {
    return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("username").toString();
  }
  
  public String getUserId(String token) {
	    return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("userid").toString();
	  }

  public String resolveToken(HttpServletRequest req) {
    String bearerToken = req.getHeader("Authorization");
    
    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
      return true;
    } catch (JwtException | IllegalArgumentException e) {
      throw new JwtCustomException("Expired or invalid JWT token", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
  
  public String getUser(HttpServletRequest req, String keyName) {
	  
	  String bearerToken = req.getHeader("Authorization");
	  logger.info("token: "+ bearerToken);
	  try {
		   if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			   String token = bearerToken.substring(7);
			   String keyValue = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get(keyName).toString();
			   return keyValue;
		    }
		    return null;
			    
	  } catch (JwtException | IllegalArgumentException e) {
	      throw new JwtCustomException("Expired or invalid JWT token", HttpStatus.INTERNAL_SERVER_ERROR);
	  }
  }
}
