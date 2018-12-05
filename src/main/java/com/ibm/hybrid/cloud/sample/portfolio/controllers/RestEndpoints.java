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

import java.util.List;

import com.ibm.hybrid.cloud.sample.portfolio.controllers.datamodel.Feedback;
import com.ibm.hybrid.cloud.sample.portfolio.controllers.datamodel.FeedbackReply;
import com.ibm.hybrid.cloud.sample.portfolio.controllers.datamodel.Portfolio;
import com.ibm.hybrid.cloud.sample.portfolio.service.FeedbackService;
import com.ibm.hybrid.cloud.sample.portfolio.service.OwnerAlreadyExistsException;
import com.ibm.hybrid.cloud.sample.portfolio.service.OwnerNotFoundException;
import com.ibm.hybrid.cloud.sample.portfolio.service.PortfolioService;
import com.ibm.watson.developer_cloud.http.HttpStatus;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/portfolio")
public class RestEndpoints {

    @Autowired
    PortfolioService portfolioService;
    @Autowired
    FeedbackService feedbackService;

    ModelMapper mapper = new ModelMapper();

    @GetMapping({"","/"}) //"" workaround for ui requesting /portfolio instead of /portfolio/
    @Secured({"ROLE_STOCKVIEWER","ROLE_STOCKTRADER"})
    public List<Portfolio> getAllPortfolios() { 
        return mapper.map(portfolioService.getAllPortfolios(), new TypeToken<List<Portfolio>>() {}.getType());
    }

    @PostMapping("/{owner}")
    @Secured("ROLE_STOCKTRADER")
    public Portfolio createPortfolio(@PathVariable String owner) throws OwnerAlreadyExistsException{
        return mapper.map(portfolioService.createNewPortfolio(owner), Portfolio.class);
    }

    @GetMapping("/{owner}")
    @Secured({"ROLE_STOCKVIEWER","ROLE_STOCKTRADER"})
    public Portfolio getPortfolio(@PathVariable String owner) 
                                  throws OwnerNotFoundException{
        return  mapper.map(portfolioService.getPortfolio(owner), Portfolio.class);
    }

    @PutMapping("/{owner}")
    @Secured("ROLE_STOCKTRADER")
    public Portfolio updatePortfolio(@PathVariable String owner,
                                    @RequestParam("symbol") String symbol, 
                                    @RequestParam("shares") int shares)
                                    throws OwnerNotFoundException{
        return  mapper.map(portfolioService.updatePortfolio(owner,symbol,shares), Portfolio.class);
    }

    @DeleteMapping("/{owner}")
    @Secured("ROLE_STOCKTRADER")
    public Portfolio deletePortfolio(@PathVariable String owner) throws OwnerNotFoundException{
        return mapper.map(portfolioService.deletePortfolio(owner), Portfolio.class);
    }  
    
    @PostMapping("/{owner}/feedback")
    @Secured("ROLE_STOCKTRADER")
    public FeedbackReply submitFeedback(@PathVariable String owner, Feedback feedback) throws OwnerNotFoundException{
        return mapper.map(feedbackService.submitFeedback(owner,feedback.getText()), FeedbackReply.class);
    }    

    @ExceptionHandler( {OwnerNotFoundException.class} )
    public ResponseEntity<String> handleNotFound() {
        return ResponseEntity.notFound().build();
    }
    @ExceptionHandler( {OwnerAlreadyExistsException.class} )
    public ResponseEntity<String> handleSAlreadyExists() {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }    
    

}
