# Delete Portfolio

Delete portfolio for a specified owner. This also deletes any associated stocks held by the portfolio.

**URL** : `/portfolio/{owner}`

**METHOD** : `DELETE`

**Auth Required** : YES

**Permissons Required** : STOCKTRADER

## Success Response

**Condition** : Portfolio can be found for specified owner.

**Code** : `200 OK`

**Content examples**

The portfolio deleted.. in summary form.

```json
{
   "loyalty" : "GOLD",
   "sentiment" : "Unknown",
   "balance" : 40.01,
   "free" : 0,
   "owner" : "fred",
   "commissions" : 9.99,
   "total" : 200000,
   "nextCommission" : 0
}
```

## Error Response

**Condition** : Portfolio not found for specified owner.

**Code** : `404 NOT FOUND`