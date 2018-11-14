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

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.ibm.hybrid.cloud.sample.portfolio.service.datamodel.Portfolio;
import com.ibm.hybrid.cloud.sample.portfolio.service.datamodel.Stock;
import com.ibm.hybrid.cloud.sample.portfolio.clients.LoyaltyClient;
import com.ibm.hybrid.cloud.sample.portfolio.clients.LoyaltyMessagingClient;
import com.ibm.hybrid.cloud.sample.portfolio.clients.StockQuoteClient;
import com.ibm.hybrid.cloud.sample.portfolio.clients.datamodel.StockQuoteReply;
import com.ibm.hybrid.cloud.sample.portfolio.repositories.PortfolioRepository;
import com.ibm.hybrid.cloud.sample.portfolio.repositories.StocksRepository;
import com.ibm.hybrid.cloud.sample.portfolio.repositories.datamodel.PortfolioRecord;
import com.ibm.hybrid.cloud.sample.portfolio.repositories.datamodel.StockRecord;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class PortfolioService {

    @Autowired
    PortfolioRepository portfolios;

    @Autowired
    StocksRepository stocks;

    @Autowired
    StockQuoteClient stockQuoteClient;

    @Autowired
    LoyaltyClient loyaltyClient;

    @Autowired
    LoyaltyMessagingClient loyaltyMessagingClient;

    @Autowired
    ModelMapper mapper;

	private static final String LOYALTY_BASIC    = "Basic";
	private static final String LOYALTY_BRONZE   = "Bronze";
	private static final String LOYALTY_SILVER   = "Silver";
	private static final String LOYALTY_GOLD     = "Gold";
	private static final String LOYALTY_PLATINUM = "Platinum";



    /**
     * Obtains a list of all portfolios, _without_ stocks. 
     * 
     * @return List of portfolios, without stocks.
     */
    public List<Portfolio> getAllPortfolios(){
        try(Stream<PortfolioRecord> records = portfolios.findAll()){
            return records.map( portfolioRecord -> mapper.map(portfolioRecord, Portfolio.class))
                          .collect(Collectors.toList());
        }
    }

    /**
     * Creates a new portfolio for the specified owner. 
     * 
     * @param owner the id to create the portfolio for
     * @return new portfolio instance
     */
    public Portfolio createNewPortfolio(String owner){

        double defaultTotal = 0.0;
        String defaultLoyalty = LOYALTY_BASIC;
        double defaultBalance = 50.0;
        double defaultCommission = 0.0;
        int defaultFree = 0;
        String defaultSentiment = "Unknown";
        double defaultNextCommission = 9.99;  

        PortfolioRecord portfolioRecord = new PortfolioRecord(owner, 
                                                              defaultTotal,
                                                              defaultLoyalty,
                                                              defaultBalance,
                                                              defaultCommission,
                                                              defaultFree,
                                                              defaultSentiment,
                                                              defaultNextCommission);
        return mapper.map(portfolios.save(portfolioRecord), Portfolio.class);
    }

    /**
     * Updates stock record with latest stock quote from stock quote service
     * 
     * @param stockRecord record to update
     * @return updated record.
     */
    private StockRecord updateStockRecord(StockRecord stockRecord){
        StockQuoteReply sqr = stockQuoteClient.getQuote(stockRecord.getSymbol());
        //TODO: confirm error handling here.. 
        if(sqr!=null){
            stockRecord.setDate(sqr.getDate());
            stockRecord.setPrice(sqr.getPrice());
            stockRecord.setTotal(stockRecord.getShares()*stockRecord.getPrice());

            //TODO: (from orig) is it ok to update during iteration?
            stocks.save(stockRecord);
        }
        return stockRecord;
    }

    /**
     * Updates loyalty level of portfolio according to external service
     * based upon total value of shares held.
     * 
     * @param p the portfolio to update the loyalty level of.
     */
    private void updateLoyaltyLevel(Portfolio p){
        double overallTotal = p.getTotal();
    
        String newLoyalty = loyaltyClient.getLoyalty(overallTotal);
        p.setLoyalty(newLoyalty);
        
        return;
    }

    /**
     * Retrieve commission rate for loyalty level.
     * 
     * @param loyalty the loyalty to return the rate for
     * @return the commission rate for the loyalty.
     */
	private double getCommission(String loyalty) {
        //TODO: (from orig) turn this into an ODM business rule
        double commission;
        switch(loyalty) {
            case LOYALTY_BRONZE:   commission = 8.99; break;
            case LOYALTY_SILVER:   commission = 7.99; break;
            case LOYALTY_GOLD:     commission = 6.99; break;
            case LOYALTY_PLATINUM: commission = 5.99; break;
            default: commission = 9.99;
        }
        return commission;
	}

    /**
     * Gets an up-to-date version of the portfolio for a user. 
     * All stock prices are retrieved at the time of the portfolio request (where possible)
     * Overall portfolio value & loyalty, and next commission rate is recalculated based on current stock values.
     * 
     * @param owner the owner of the portfolio
     * @return up to date portfolio
     */
    @Transactional
    public Portfolio getPortfolio(String owner) throws OwnerNotFoundException{
        PortfolioRecord pr;
        if((pr = portfolios.findByOwner(owner)) != null ){
            Portfolio p = mapper.map(pr, Portfolio.class);
            try(Stream<StockRecord> stockRecords = stocks.findByOwner(owner)){

                p.setStocks(
                    stockRecords.map( stockRecord -> updateStockRecord(stockRecord))
                                .map( stockRecord -> mapper.map(stockRecord, Stock.class))
                                .collect(Collectors.toList())
                );

                p.setTotal(p.getStocks().stream().mapToDouble(stock -> stock.getTotal()).sum());   

                String oldLoyalty = p.getLoyalty();
                updateLoyaltyLevel(p);
                if(!oldLoyalty.equalsIgnoreCase(p.getLoyalty())){
                    loyaltyMessagingClient.sendLoyaltyUpdate(owner, oldLoyalty, p.getLoyalty());
                }
                
                if(p.getFree()>0){
                    p.setNextCommission(getCommission(p.getLoyalty()));
                }else{
                    p.setNextCommission(0.0);
                }
            }
            return p;
        }else{
            throw new OwnerNotFoundException();
        }
    }

	private double processCommission(String owner) throws OwnerNotFoundException{
        PortfolioRecord pr = portfolios.findByOwner(owner);
        if(pr!=null){
            String loyalty = pr.getLoyalty();
            double commission = getCommission(loyalty); 
            int free = pr.getFree();  
            if(free > 0){
                pr.setFree(free-1);
            }else{
                pr.setCommissions(pr.getCommissions()+commission);
                pr.setBalance(pr.getBalance()-commission);
            }
            portfolios.save(pr);
            return commission;
        }
        throw new OwnerNotFoundException();
    }
   

    @Transactional
    public Portfolio updatePortfolio(String owner, String symbol, int shares) throws OwnerNotFoundException{
        double commission = processCommission(owner);

        StockRecord stock = stocks.findByOwnerAndSymbol(owner,symbol);
        if(stock!=null){
            stock.setShares(stock.getShares()+shares);
            stock.setCommission(stock.getCommission()+commission);
        }

        if(stock.getShares()>0){
            //save updates to the stock.
            stocks.save(stock);
        }else{
            //if the stock count is now 0 (or negative), we remove this stock from the user.
            stocks.delete(stock);
        }

        return getPortfolio(owner);        
    }

    /**
     * Deletes a portfolio
     * 
     * @param owner the owner of the portfolio to be deleted
     * @return the deleted portfolio, without stocks
     */
    public Portfolio deletePortfolio(String owner) throws OwnerNotFoundException{
        PortfolioRecord pr = portfolios.findByOwner(owner);
        if(pr !=null ){
            portfolios.delete(pr);
            return mapper.map(pr,Portfolio.class);
        }else{
            throw new OwnerNotFoundException();
        }
    }

    //TODO: this cannot return Portfolio .. needs to return something that includes the feedback message
    public Portfolio submitFeedback(String owner, String input){
        //TODO: unimplemented.
        return null;
    }
}