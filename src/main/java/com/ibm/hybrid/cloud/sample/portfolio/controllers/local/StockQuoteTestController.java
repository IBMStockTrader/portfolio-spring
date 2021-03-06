package com.ibm.hybrid.cloud.sample.portfolio.controllers.local;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Local stand in for stock-quote service when developing spring app away from rest of StockTrader.
 */
@RestController
@Profile({"dev","boost"})
public class StockQuoteTestController {

    AtomicInteger seq = new AtomicInteger(0);

    @GetMapping("/stock-quote/{symbol}")
    @Secured({"STOCKTRADER","STOCKVIEWER"})
    public ResponseEntity<String> getQuote(@PathVariable String symbol){    
        LocalDateTime datetime = LocalDateTime.now();
        String now = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(datetime);
        double price = 20.0 + (seq.incrementAndGet() %10);
        String result = "{\"symbol\": \""+symbol+"\", \"date\": \""+now+"\", \"price\": \""+100.0+"\"}";
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}