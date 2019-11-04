package com.sicc.sample.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.sicc.sample.mapper.UserMapper;
import com.sicc.sample.security.JwtCustomException;
import com.sicc.sample.vo.LoginVO;
import com.sicc.sample.vo.UserVO;

import io.jsonwebtoken.JwtException;

@Service
public class UserService {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
    private UserMapper userMapper;

    
    public UserVO getUserOne(LoginVO loginVO) {
        return userMapper.getUserOne(loginVO);
    }
    
    public UserVO getUser(String userId) {
  	  
      try {
		  UserVO userVO = new UserVO();
		  
		  if (!"".equals(userId) && userId != null) {
			  userVO = userMapper.getUserDetail(userId);
		  }
		  
		  return userVO;
		  
  	  } catch (JwtException | IllegalArgumentException e) {
  	      throw new JwtCustomException("Expired or invalid JWT token", HttpStatus.INTERNAL_SERVER_ERROR);
  	  }
    }
    
    public int insertUser(UserVO user) {
    	  
        try {
  		  
          int result = userMapper.insertUser(user);
  		  return result;
  		  
    	  } catch (JwtException | IllegalArgumentException e) {
    	      throw new JwtCustomException("Expired or invalid JWT token", HttpStatus.INTERNAL_SERVER_ERROR);
    	  }
      }
}
