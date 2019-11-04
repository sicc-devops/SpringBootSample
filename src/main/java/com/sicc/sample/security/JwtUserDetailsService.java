package com.sicc.sample.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sicc.sample.service.UserService;
import com.sicc.sample.vo.LoginVO;
import com.sicc.sample.vo.UserVO;

@Service
public class JwtUserDetailsService implements UserDetailsService {

	 @Autowired
	 UserService userService;
	
	  @Override
	  public UserDetails loadUserByUsername(String userid) throws UsernameNotFoundException {
		LoginVO param = new LoginVO();
		param.setUserId(userid);
		final UserVO user = userService.getUserOne(param);
	    
	    if (user == null) {
	      throw new UsernameNotFoundException("User '" + userid + "' not found");
	    }

	    List<JwtRole> roles = new ArrayList<JwtRole>();
	    
	    return org.springframework.security.core.userdetails.User//
	        .withUsername(userid)//
	        .password(user.getPassword())//
	        .authorities(roles)//
	        .accountExpired(false)//
	        .accountLocked(false)//
	        .credentialsExpired(false)//
	        .disabled(false)//
	        .build();
	  }
}
