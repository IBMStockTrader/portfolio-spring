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

import java.io.IOException;
import java.security.Key;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SigningKeyResolverAdapter;

/**
 * Filter that identifies presence of a JWT in the Authorization Header of request,
 * and creates an _unauthenticated_ JwtTokenAuthentication object that is passed to the 
 * AuthenticationManager for verification. 
 */
public class JwtAuthFilter extends OncePerRequestFilter{
    private final String header = "Authorization";

    private final AuthenticationManager authenticationManager;

    private static class IdentityExtractor extends SigningKeyResolverAdapter {
        private String subject = null;
        @Override
        public Key resolveSigningKey(JwsHeader header, Claims claims) {
            this.subject = claims.getSubject();
            return null; // will throw exception, can be caught in caller
        }
    }

    public JwtAuthFilter(AuthenticationManager authenticationManager){
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
         HttpServletResponse response, FilterChain filterChain) 
         throws ServletException, IOException {
        String authHeader = request.getHeader(header);
        System.out.println("Looking for header .. got "+String.valueOf(authHeader));
        if(authHeader!=null){
            String parts[] = authHeader.split(" "); //Bearer ejkfjkfejkfem
            if(parts.length==2){
                String encodedToken = parts[1];
                IdentityExtractor ie = new IdentityExtractor();
                try{
                    Jwts.parser().setSigningKeyResolver(ie).parseClaimsJws(encodedToken).getBody();
                }catch(IllegalArgumentException e){
                    //expected, as we return null in the key resolver.
                    String id = ie.subject;
                    System.out.println("ID was "+id);
                    Authentication authentication = authenticationManager.authenticate(new JwtTokenAuthentication(id,encodedToken));
                    SecurityContextHolder.getContext().setAuthentication(authentication);                    
                }catch(Exception e){
                    //ignore the error, maybe this isn't a valid jwt
                    //either way the request remains unauthenticated.
                    System.err.println("ERROR");
                    e.printStackTrace();
                }
            }
        }
        filterChain.doFilter(request,response);
    }
}