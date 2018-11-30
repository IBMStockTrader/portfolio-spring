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

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

/**
 * Auth provider that accepts JwtTokenAuthentications, and validates
 * the token embedded to ensure it meets the requirements. 
 * 
 * issuer/audience must match expected
 * signature must be valid
 * valid from/until must be valid
 * 
 * If present, 'roles' will be used to configure the roles to be granted for the user.
 * 
 * For compatibility with MP Impl of Portfolio, if roles is absent, roles will be determined from hard coded set
 * (this could be externalised into config, but seems unwise as it's only a stop-gap measure).
 */
@Component
public class JwtAuthProvider implements AuthenticationProvider {

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.audience}")
    private String audience;

    @Value("${jwt.allowedSkew:30}")
    private long skew;

    @Value("${jwt.publicKey}")
    private String key;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String id = authentication.getName();
        String token = authentication.getCredentials().toString();

        try{
            Claims claims = Jwts.parser()
                .requireAudience(audience)
                .requireIssuer(issuer)
                .setAllowedClockSkewSeconds(skew)
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();

            String rolesStr = claims.get("roles", String.class);

            //eventually, roles will come in via the jwt, but for now they need to be looked up per user
            //as per the MP-Portfolio impl.
            if(rolesStr==null || rolesStr.isEmpty()){
                rolesStr = getRolesForId(id);
            }

            List<GrantedAuthority> grantedAuths = AuthorityUtils.commaSeparatedStringToAuthorityList(rolesStr);

            return new JwtTokenAuthentication(id, token, grantedAuths);
        }catch(JwtException ex){
            ex.printStackTrace();
            //jwt was not valid.
            throw new BadCredentialsException("Unable to validate JWT");
        }
    }

    @Override
    public boolean supports(Class<?> authentication){
        return authentication.isAssignableFrom(JwtTokenAuthentication.class);
    }

    //temp method to supply roles.
    public String getRolesForId(String id){
        switch(id){
            case "admin" : return "ADMIN";
            case "stock" : return "STOCKTRADER";
            case "debug" : return "STOCKTRADER";
            case "read" : return "STOCKVIEWER";
            case "other" : return "OTHER";
            case "jalcorn@us.ibm.com" : return "STOCKTRADER";
            default : return "";
        }
    }
}