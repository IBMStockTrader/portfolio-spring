# Create Portfolio

Creates a new portfolio for a specified owner.

**URL** : `/portfolio/{owner}`

**METHOD** : `POST`

**Auth Required** : YES

**Permissons Required** : STOCKTRADER

## Success Response

**Condition** : If no conflicts exist, and a new Portfolio has been created.

**Code** : `200 OK`

**Content examples**


```json
{
   "balance" : 50,
   "commissions" : 0,
   "sentiment" : "Unknown",
   "nextCommission" : 0,
   "total" : 0,
   "owner" : "fred",
   "loyalty" : "Basic",
   "free" : 0
}
```

## Error Response

**Condition** : If the owner already has a portfolio, 

**Code** : `409 CONFLICT`