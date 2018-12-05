# Update Portfolio

Adjust the amount held of a given stock symbol for the owners portfolio.

**URL** : `/portfolio/{owner}?symbol={symbol}&shares={quantity}`

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
   "stocks" : {
    "MSFT": {
      "symbol": "MSFT",
      "shares": 40,
      "commission": 79.92,
      "price": 100,
      "total": 4000,
      "date": "2018-12-04"
    },
    "IBM": {
      "symbol": "IBM",
      "shares": 200,
      "commission": 29.97,
      "price": 100,
      "total": 20000,
      "date": "2018-12-04"
    },
    "AAPL": {
      "symbol": "AAPL",
      "shares": 20,
      "commission": 9.99,
      "price": 100,
      "total": 2000,
      "date": "2018-12-04"
    }
   },
   "nextCommission" : 0,
   "loyalty" : "BRONZE",
   "owner" : "wilma",
   "free" : 0,
   "total" : 26000
}
```