package com.sicc.sample.security;

import org.springframework.security.core.GrantedAuthority;

public enum JwtRole implements GrantedAuthority {
	  ROLE_ADMIN, ROLE_CLIENT;

	  public String getAuthority() {
	    return name();
	  }

}
