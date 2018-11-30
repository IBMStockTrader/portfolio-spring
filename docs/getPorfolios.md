# Get Summary Portfolios

Get all portfolios in summary form (without stocks).

**URL** : `/`

**METHOD** : `GET`

**Auth Required** : YES

**Permissons Required** : STOCKTRADER, STOCKVIEWER

## Success Response

**Code** : `200 OK`

**Content examples**

```json
[
   {
      "owner" : "fred",
      "total" : 0,
      "sentiment" : "Unknown",
      "nextCommission" : 0,
      "free" : 0,
      "loyalty" : "BASIC",
      "commissions" : 0,
      "balance" : 50
   },
   {
      "total" : 26000,
      "owner" : "wilma",
      "sentiment" : "Unknown",
      "free" : 0,
      "nextCommission" : 0,
      "balance" : -69.88,
      "commissions" : 119.88,
      "loyalty" : "BRONZE"
   }
]
```

