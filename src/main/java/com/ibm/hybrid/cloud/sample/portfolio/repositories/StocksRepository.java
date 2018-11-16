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
package com.ibm.hybrid.cloud.sample.portfolio.repositories;

import java.util.stream.Stream;

import com.ibm.hybrid.cloud.sample.portfolio.repositories.datamodel.StockRecord;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface StocksRepository extends Repository<StockRecord, String> {
    @Query("SELECT Stock.owner AS owner, Stock.symbol AS symbol, Stock.shares AS shares, Stock.price AS price, Stock.total AS total, Stock.dateQuoted AS dateQuoted, Stock.commission AS commission FROM Stock WHERE Stock.owner=:owner")
    Stream<StockRecord> findByOwner(@Param("owner") String owner);

    @Query("SELECT Stock.owner AS owner, Stock.symbol AS symbol, Stock.shares AS shares, Stock.price AS price, Stock.total AS total, Stock.dateQuoted AS dateQuoted, Stock.commission AS commission FROM Stock WHERE Stock.owner=:owner AND Stock.symbol=:symbol")
    StockRecord findByOwnerAndSymbol(@Param("owner")String owner, @Param("symbol") String symbol);

    @Modifying
    @Query("DELETE FROM Stock WHERE Stock.owner=:owner AND Stock.symbol=:symbol")
    void delete(@Param("owner")String owner, @Param("symbol") String symbol);

    //StockRecord save(StockRecord record);

    @Modifying
    @Query("INSERT INTO Stock (owner, symbol, shares, commission, price, total, datequoted) VALUES ( :owner, :symbol, :shares, :commission, :price, :total, :datequoted )")
    boolean insert(@Param("owner")String owner, 
                   @Param("symbol") String symbol, 
                   @Param("shares") int shares,
                   @Param("commission") double commission,
                   @Param("price") double price,
                   @Param("total") double total,
                   @Param("datequoted") String datequoted);
    @Modifying
    @Query("UPDATE Stock SET shares=:shares, commission=:commission, price=:price, total=:total, datequoted=:datequoted WHERE owner=:owner AND symbol=:symbol")
    boolean update(@Param("owner")String owner, 
                    @Param("symbol") String symbol, 
                    @Param("shares") int shares,
                    @Param("commission") double commission,
                    @Param("price") double price,
                    @Param("total") double total,
                    @Param("datequoted") String datequoted);                   

}
