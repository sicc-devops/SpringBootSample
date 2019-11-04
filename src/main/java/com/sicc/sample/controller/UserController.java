package com.sicc.sample.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sicc.sample.security.JwtTokenProvider;
import com.sicc.sample.service.UserService;
import com.sicc.sample.vo.UserVO;

@RestController
@RequestMapping("/user")
public class UserController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	 @Autowired
	 JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	UserService userService;

	 @Autowired
	 PasswordEncoder passwordEncoder;
	 
	@RequestMapping(value="/detail", method = RequestMethod.GET)
	public Map<String, Object> getUserDetail(HttpServletRequest req) {
		Map<String, Object> result = new HashMap<String, Object>();
		
	    try {
	    	UserVO userVO = new UserVO();
			String bearerToken = req.getHeader("Authorization");
			
			if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
				String userid = jwtTokenProvider.getUser(req,"userid");
				userVO = userService.getUser(userid);
			}

	   	 	result.put("ResultCode", "200");
	        result.put("ResultMsg", "success");
			result.put("UserVO", userVO);
			return result;
		  
	    } catch(Exception e) {
	    	logger.debug("context", e);
	   	 	e.printStackTrace();
	   	 	result.put("ResultCode", "900");
	        result.put("ResultMsg", "fail");
	        return result;
	    }
		
	}
}
