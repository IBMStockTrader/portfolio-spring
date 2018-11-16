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
package com.ibm.hybrid.cloud.sample.portfolio;

import com.ibm.hybrid.cloud.sample.portfolio.repositories.PortfolioRepository;
import com.ibm.hybrid.cloud.sample.portfolio.repositories.datamodel.PortfolioRecord;
import com.ibm.hybrid.cloud.sample.portfolio.repositories.datamodel.StockRecord;
import com.ibm.hybrid.cloud.sample.portfolio.service.datamodel.Stock;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.data.relational.core.mapping.NamingStrategy;
import org.springframework.data.relational.core.mapping.event.BeforeSaveEvent;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableJms
@EnableJdbcRepositories("com.ibm.hybrid.cloud.sample.portfolio.repositories")
public class PortfolioApplication {

	public static void main(String[] args) {
		SpringApplication.run(PortfolioApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder){
		return builder.build();
	}

	/*
	* Our dao has the date column named 'datequoted' to match the db column
	* Our dto has the date field named 'date' to meet the json requirements.
	* ModelMapper uses a property map as a type safe way to add mappings 
	* from one type to another. 
	*/
	@Bean
	public ModelMapper mapper(){
		ModelMapper mapper = new ModelMapper();
		PropertyMap<StockRecord, Stock> dateMap = new PropertyMap<StockRecord, Stock>() {
			protected void configure() {
			  map().setDate(source.getDatequoted());
			}
		  };
		mapper.addMappings(dateMap);
		return mapper;
	}

	/*
	* Our database table names don't match our dao object names, Spring Data
	* allows us to alter the mappings uing a NamingStrategy. 
    *
	* We've named our DAO's to end in 'Record' to avoid confusion with our 
	* DTO's and Service Objects.
	*/
	@Bean
    NamingStrategy namingStrategy() {
        return new NamingStrategy() {
            @Override
            public String getQualifiedTableName(Class<?> c){
				String simpleName = c.getSimpleName();
				if(simpleName.endsWith("Record")){
					simpleName = simpleName.substring(0,simpleName.length() - "Record".length());
				}
				return simpleName;
			}
        };
	}

}
