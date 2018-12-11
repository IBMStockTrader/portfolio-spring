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

import com.ibm.hybrid.cloud.sample.portfolio.clients.datamodel.StockQuoteReply;
import com.ibm.hybrid.cloud.sample.portfolio.jwt.AuthHeaderPropagatingInterceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class StockQuoteClient {

    @Autowired
    AuthHeaderPropagatingInterceptor headerPropagation;

    @Autowired
    RestTemplate restTemplate;

    @Value("${quoteservice.url}")
    String quoteUrl;
    
    public StockQuoteReply getQuote(String symbol){
        //StockQuote requires us to propagate the security info.
        restTemplate.getInterceptors().add(headerPropagation);
        StockQuoteReply sqr = restTemplate.getForObject(quoteUrl+"/"+symbol,StockQuoteReply.class);

        return sqr;
    }
}