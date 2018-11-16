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
package com.ibm.hybrid.cloud.sample.portfolio.repositories.datamodel;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;

public class StockRecord implements Persistable<String> {

    @Transient
    private boolean isNew;

    @Id
    private String owner;
    private String symbol;
    private int shares;
    private double commission;
    private double price;
    private double total;
    private String datequoted;

    public StockRecord() {
        isNew=false;
    }

    public StockRecord(String owner, String initialSymbol, int initialShares, double initialCommission,
                 double initialPrice, double initialTotal, String initialDate) {
        setOwner(owner);
        setSymbol(initialSymbol);
        setShares(initialShares);
        setCommission(initialCommission);
        setPrice(initialPrice);
        setTotal(initialTotal);
        setDatequoted(initialDate);
        isNew=false;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String newOwner) {
        owner = newOwner;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String newSymbol) {
        symbol = newSymbol;
    }

    public int getShares() {
        return shares;
    }

    public void setShares(int newShares) {
        shares = newShares;
    }

    public double getCommission() {
        return commission;
    }

    public void setCommission(double newCommission) {
        commission = newCommission;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double newPrice) {
        price = newPrice;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double newTotal) {
        total = newTotal;
    }

    public String getDatequoted() {
        return datequoted;
    }

    public void setDatequoted(String newDate) {
        datequoted = newDate;
    }

    @Override
    public String getId() {
        return symbol;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean n){
        isNew = n;
    }

}