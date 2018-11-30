/*
       Copyright 2018 IBM Corp All Rights Reserved
   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at
       http://www.apache.org/licenses/LICENSE-2.0
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package com.ibm.hybrid.cloud.sample.portfolio.jwt;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

/**
 * Implementation of Spring Authentication used to represent JWT based auth.
 */
public class JwtTokenAuthentication extends AbstractAuthenticationToken {

    private static final long serialVersionUID = -5807714167973987590L;

    private String token;
    private final String id;

    public JwtTokenAuthentication(String id, String token) {
        super(null);
        this.token=token;
        this.id=id;
        setAuthenticated(false);
    }

	public JwtTokenAuthentication(String id, String token,
			Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
        this.token=token;
        this.id=id;
		super.setAuthenticated(true); // super, because we override
	}


    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return id;
    }

    @Override
    public String getName() {
        return id;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
		if (isAuthenticated) {
			throw new IllegalArgumentException(
					"Token authenticated state cannot be made true, use appropriate constructor instead.");
		}

		super.setAuthenticated(false);
    }
    
    @Override
	public void eraseCredentials() {
        super.eraseCredentials();
		token = null;
	}
}