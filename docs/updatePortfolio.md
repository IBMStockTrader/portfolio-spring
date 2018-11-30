# Update Portfolio

Adjust the amount held of a given stock symbol for the owners portfolio.

**URL** : `/{owner}?symbol={symbol}&shares={quantity}`

**METHOD** : `PUT`

**Auth Required** : YES

**Permissons Required** : STOCKTRADER

## Success Response

**Code** : `200 OK`

**Content examples**

```json
{
   "balance" : -69.88,
   "sentiment" : "Unknown",
   "commissions" : 119.88,
   "stocks" : [
      {
         "price" : 100,
         "symbol" : "AAPL",
         "date" : "2018-11-23",
         "total" : 2000,
         "shares" : 20,
         "commission" : 9.99
      },
      {
         "price" : 100,
         "symbol" : "IBM",
         "date" : "2018-11-23",
         "total" : 20000,
         "shares" : 200,
         "commission" : 29.97
      },
      {
         "date" : "2018-11-23",
         "symbol" : "MSFT",
         "price" : 100,
         "commission" : 79.92,
         "shares" : 40,
         "total" : 4000
      }
   ],
   "nextCommission" : 0,
   "loyalty" : "BRONZE",
   "owner" : "wilma",
   "free" : 0,
   "total" : 26000
}
```