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

import com.ibm.hybrid.cloud.sample.portfolio.clients.datamodel.LoyaltyDecision;
import com.ibm.hybrid.cloud.sample.portfolio.clients.datamodel.LoyaltyQuery;
import com.ibm.hybrid.cloud.sample.portfolio.clients.datamodel.LoyaltyResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class LoyaltyClient {
    @Autowired
    RestTemplate restTemplate;

    @Value("${loyalty.url:http://odmtrader2-ibm-odm-dev:9060/DecisionService/rest/ICP_Trader_Dev_1/determineLoyalty}")
    String loyaltyUrl;
    @Value("${loyalty.id}")
    String loyaltyId;
    @Value("${loyalty.pwd}")
    String loyaltyPwd;        

    public String getLoyalty(double overallTotal){

        LoyaltyDecision ld = new LoyaltyDecision(overallTotal);
        LoyaltyQuery lq = new LoyaltyQuery(ld);

        restTemplate.getInterceptors().add( new BasicAuthenticationInterceptor(loyaltyId,loyaltyPwd));
        LoyaltyResponse lr = restTemplate.postForObject(loyaltyUrl,lq,LoyaltyResponse.class);
        
        return lr.getTheLoyaltyDecision().getLoyalty();
    }
}