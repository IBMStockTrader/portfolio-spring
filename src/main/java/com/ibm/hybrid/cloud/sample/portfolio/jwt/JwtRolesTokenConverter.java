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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class JwtRolesTokenConverter extends DefaultUserAuthenticationConverter {

        @Override
        public Authentication extractAuthentication(Map<String, ?> map) {
          if (map.containsKey("sub")) {
            String username = (String) map.get("sub");
            Collection<? extends GrantedAuthority> authorities = getAuthorities(map);
            return new UsernamePasswordAuthenticationToken(username, null, authorities);
          }
          return null;
        }
      
       
        /**
         * Parse MP-JWT groups, and create appropriate spring authorities
         * @param map
         * @return
         */
        private Collection<? extends GrantedAuthority> getAuthorities(Map<String, ?> map) {            
          Object gObj = map.get("groups");                    
          if( gObj != null && List.class.isAssignableFrom(gObj.getClass())){
            @SuppressWarnings("unchecked")
            List<String> groups = (List<String>)gObj;

            //uppercase & prefix each group id from JWT with "ROLE_"
            List<String> authStrs = new ArrayList<>();
            for(String as : groups){
                authStrs.add("ROLE_"+as.toUpperCase().trim());
            }

            //return Spring roles.
            return AuthorityUtils.commaSeparatedStringToAuthorityList(StringUtils
					.collectionToCommaDelimitedString((Collection<?>) authStrs).toUpperCase());
          }        
          return null;
        }
      
      }