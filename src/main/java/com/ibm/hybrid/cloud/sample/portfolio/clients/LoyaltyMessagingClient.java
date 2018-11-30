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
package com.ibm.hybrid.cloud.sample.portfolio.clients;

import com.ibm.hybrid.cloud.sample.portfolio.clients.datamodel.LoyaltyChange;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class LoyaltyMessagingClient {
    @Autowired
    JmsTemplate jmsTemplate;

    @Value("${loyalty.queue.name}")
    String queueName;

    @Bean // Serialize message content to json using TextMessage
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        //not setting _type because want to allow Spring to receive as a text message for debug.
        //converter.setTypeIdPropertyName("_type");
        return converter;
    }

    public void sendLoyaltyUpdate(String owner, String oldLoyalty, String newLoyalty){
        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
        String id = currentAuth.getName();

        LoyaltyChange lc = new LoyaltyChange(owner,oldLoyalty,newLoyalty);
        if(id!=null)
            lc.setId(id);
            
        jmsTemplate.convertAndSend(queueName,lc);
    }
}