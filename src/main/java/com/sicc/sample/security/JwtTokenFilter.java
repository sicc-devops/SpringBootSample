package com.sicc.sample.security;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

public class JwtTokenFilter extends GenericFilterBean {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
    private JwtTokenProvider jwtTokenProvider;

    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
        throws IOException, ServletException {
    	
    	 try {
    		 String token = jwtTokenProvider.resolveToken((HttpServletRequest) req);
    		 String url = ((HttpServletRequest)req).getRequestURL().toString();
    		 
    		 if(token!=null && url.indexOf("/auth/") != -1) {

    		 } else {
    			 if (token != null && jwtTokenProvider.validateToken(token)) {
     	            Authentication auth = jwtTokenProvider.getAuthentication(token);

     	            if (auth != null) {
     	                SecurityContextHolder.getContext().setAuthentication(auth);
     	            }
    			 }
    		 }
    		 
    	        
          
          } catch(JwtCustomException ex) {
        	  log.debug("context",ex);

        	  res.setCharacterEncoding("utf-8");
        	  res.setContentType("application/json");
              PrintWriter out = res.getWriter();
              
              JSONObject jsonObject;
			try {
				jsonObject = new JSONObject("{'code':'400','message':'Expired or invalid JWT token'}");
				out.print(jsonObject);
	            out.flush();
	            out.close();
	            return;
	            
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
             
              
        	  
          }
    	 
    	 filterChain.doFilter(req, res);
    	 
       
    }

}
