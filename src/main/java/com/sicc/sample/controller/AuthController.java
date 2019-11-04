package com.sicc.sample.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.sicc.sample.security.JwtRole;
import com.sicc.sample.security.JwtTokenProvider;
import com.sicc.sample.service.UserService;
import com.sicc.sample.vo.LoginVO;
import com.sicc.sample.vo.UserVO;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	 @Autowired
	 UserService userService;
	 
	 @Autowired
	 PasswordEncoder passwordEncoder;
	 
	 @Autowired
	 JwtTokenProvider jwtTokenProvider;
	 
	 @PostMapping("/login")
	 public Map<String, Object> signin(@RequestBody LoginVO loginVO, HttpServletRequest req)  {
		 Map<String, Object> result = new HashMap<String, Object>();
		 
	     try {
	    	 logger.info("----------- login start --------------");
	    	 logger.info(">>>>user :"+ loginVO.getUserName());
	    	 
	         UserVO user = userService.getUserOne(loginVO);
	         
	         if(user != null ) {
	        	
		    	// 비밀번호 확인
	        	String encriptPw = passwordEncoder.encode(loginVO.getPassword());
		        
	        	if (!passwordEncoder.matches(loginVO.getPassword(), user.getPassword())) {
	   	    	 	result.put("code", "999");
	   	    	 	result.put("message", "로그인 실패");
	   	    	 	return result;
	    		}
	            
		        String userName = user.getUserName();
		        String userId = user.getUserId();
	            
		        logger.info(">>>>>>>>>> user id : "+user.getUserId());
		        logger.info(">>>>>>>>>> user name : "+user.getUserName());
		        
	        	List<JwtRole> roles = new ArrayList<JwtRole>();
		        String token = jwtTokenProvider.createToken(user, roles);
		        
		        result.put("token", token);
		        result.put("code", "200");
		        result.put("message", "success");

	         }else {
		        result.put("ResultCode", "404");
			    result.put("ResultMsg", "사용자를 찾을 수 없습니다.");	        	 
	         }
         
	         return result;
	     } catch(Exception e) {
	    	 logger.debug("context", e);
	    	 e.printStackTrace();
	    	 result.put("ResultCode", "900");
	         result.put("ResultMsg", "fail");
	         return result;
	     }
	 }
	 
	@RequestMapping(value="/Register", method = RequestMethod.POST)
	public Map<String, Object> insertUser(HttpServletRequest req, @RequestBody UserVO userVO) {
		Map<String, Object> result = new HashMap<String, Object>();
		
	    try {
	    	
	    	String encPass = passwordEncoder.encode(userVO.getPassword());
	    	userVO.setPassword(encPass);
	    	
	    	int insert = userService.insertUser(userVO);
	    	logger.info(">>>>>>>> result : " + insert);
	    	if (insert==1) {
		   	 	result.put("ResultCode", "200");
		        result.put("ResultMsg", "success");
	    	}

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
