# Get Detailed Portfolio

Get all detailed portfolio for a specified owner

**URL** : `/portfolio/{owner}`

**METHOD** : `GET`

**Auth Required** : YES

**Permissons Required** : STOCKTRADER, STOCKVIEWER

## Success Response

**Condition** : Portfolio can be found for specified owner.
**Code** : `200 OK`

**Content examples**

Example for a user with multiple stocks.. 

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

Example for a user without stocks..

```json
{
   "free" : 0,
   "owner" : "fred",
   "balance" : 50,
   "nextCommission" : 0,
   "stocks" : [],
   "loyalty" : "BASIC",
   "total" : 0,
   "commissions" : 0,
   "sentiment" : "Unknown"
}
```

## Error Response

**Condition** : No portfolio was located for specified owner.

**Code** : `404 NOT FOUND`

