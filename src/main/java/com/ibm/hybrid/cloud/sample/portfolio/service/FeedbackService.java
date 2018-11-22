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
package com.ibm.hybrid.cloud.sample.portfolio.service;

import java.util.Comparator;
import java.util.Optional;

import com.ibm.hybrid.cloud.sample.portfolio.repositories.PortfolioRepository;
import com.ibm.hybrid.cloud.sample.portfolio.repositories.datamodel.PortfolioRecord;
import com.ibm.hybrid.cloud.sample.portfolio.service.datamodel.Feedback;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.DocumentAnalysis;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneOptions;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneScore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FeedbackService{

    @Autowired 
    ToneAnalyzer toneAnalyzerService;

    @Autowired
    PortfolioRepository portfolios;
    public Feedback submitFeedback(String owner, String input){
        
        PortfolioRecord pr = portfolios.findById(owner);
        
        ToneOptions toneRequest = new ToneOptions.Builder().text(input).sentences(false).build();
        DocumentAnalysis documentAnalysis = toneAnalyzerService.tone(toneRequest).execute().getDocumentTone();

        //as per MP Portfolio impl, if we get back multiple tones, we only want the highest scoring one.
        Optional<ToneScore> result = documentAnalysis.getTones().stream().max(Comparator.comparing( ToneScore::getScore ));

        String sentiment = "Unknown";
        if(result.isPresent()){
            sentiment = result.get().getToneName();
        }

        Feedback feedback = getFeedback(owner, sentiment);
        
        int freeTrades = feedback.getFree();
        pr.setSentiment(feedback.getSentiment());
        pr.setFree(feedback.getFree()); //Note this OVERWRITES previous free trade count.

        portfolios.save(pr);

        return feedback;
    }

    //Eventually to be an external service invoke.
	private Feedback getFeedback(String owner, String sentiment) {
		int freeTrades = 1;
		String message = "Thanks for providing feedback.  Have a free trade on us!";

		if ("Anger".equalsIgnoreCase(sentiment)) {
			freeTrades = 3;
			message = "We're sorry you are upset.  Have three free trades on us!";
		}

		Feedback feedback = new Feedback(message, freeTrades, sentiment);
		return feedback;
	}    
}