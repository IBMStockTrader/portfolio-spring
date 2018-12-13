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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Component
@ControllerAdvice
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private final static Logger logger = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException ) throws IOException, ServletException { 
    handleDeniedException(request,response,authException);
  }

  @ExceptionHandler(value = { AccessDeniedException.class })
  public void commence(HttpServletRequest request, HttpServletResponse response, AccessDeniedException ex ) throws IOException {
    handleDeniedException(request, response,ex);
  }

  private String sanitise(String msg, String jwt) {
    if (jwt !=null && msg.contains(jwt) ){
      msg = msg.replace(jwt, "");
    }
    return msg;
  }

  /**
    * driven with InsufficientAuthenticationException the JWT is absent, badly signed, etc
    * driven with AccessDeniedException if the method was @Secured (or equiv) with a role the JWT did not grant.
    */
  private void handleDeniedException(HttpServletRequest request, HttpServletResponse response, Exception ex) throws IOException{


    String authHeader = request.getHeader("Authorization");
    String jwt = null;
    if(authHeader!=null){
      String parts[] = authHeader.split(" ");
      if(parts.length >1){
        jwt = parts[1];
      }
    }

    //log upstream reason for failure.
    String exMsg = ex.getClass().getSimpleName()+" : "+sanitise(ex.getMessage(),jwt);
    Throwable t = ex;
    while(t.getCause()!=null){
      t = t.getCause();
      exMsg += "("+t.getClass().getSimpleName()+" : "+sanitise(t.getMessage(),jwt)+")";
    }

    //AccessDenied means user auth was accepted, but permissions check failed.
    //log user & perm.
    if(ex instanceof AccessDeniedException){
      SecurityContext sc = SecurityContextHolder.getContext();
      Authentication a = sc.getAuthentication();
      exMsg += "(User:"+a.getName()+" Auth:"+a.getAuthorities()+")";
    }

    //Record failed attempt
    logger.warn("Failed authentication attempt for resource "+request.getRequestURL()+" "+exMsg);

    //Response for user. Adjust detail as required.
    response.setContentType("application/json");
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.getOutputStream().println("{ \"error\": \"" + sanitise(ex.getMessage(),jwt) + "\" }");
  }
}