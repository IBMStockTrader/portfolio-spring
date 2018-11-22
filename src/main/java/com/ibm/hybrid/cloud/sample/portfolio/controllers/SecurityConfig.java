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
package com.ibm.hybrid.cloud.sample.portfolio.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                    .disable()
                .authorizeRequests()
                    .anyRequest().authenticated()
                .and()
                    .httpBasic()
                .and()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    @Autowired
    @SuppressWarnings("deprecation")
    protected void configure(AuthenticationManagerBuilder  auth) throws Exception {
        //only using NoOpPassword encoded because this an example. 
        //no real deployment would use a table of userids/passwords in this manner
        //with the passwords in cleartext in the source.
        auth.inMemoryAuthentication().passwordEncoder(NoOpPasswordEncoder.getInstance())
                .withUser("admin")
                    .password("admin")
                    .roles("ADMIN")
            .and()                    
                .withUser("stock") 
                    .password("trader")
                    .roles("STOCKTRADER")
            .and()                    
                .withUser("debug") 
                    .password("debug")
                    .roles("STOCKTRADER")
            .and()                    
                .withUser("read")
                    .password("only")
                    .roles("STOCKVIEWER")
            .and()                    
                .withUser("other")
                    .password("other")
                    .roles("OTHER")                                                                        
            .and()                    
                .withUser("jalcorn@us.ibm.com") 
                    .password("test")
                    .roles("STOCKTRADER");
    }
}