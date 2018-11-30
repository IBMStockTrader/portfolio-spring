# Submit Feedback

Submit feedback about the StockTrader service from an owner.

**URL** : `/{owner}/feedback`

**METHOD** : `POST`

**Auth Required** : YES

**Permissons Required** : STOCKTRADER

**Data constraints**

```json
{
    "text": "[unicode 128k max]",
}
```

**Data example**

```json
{
    "text": "StockTrader makes me happy happy happy",
}
```

## Success Response

**Code** : `200 OK`

**Content examples**

```json
{
    "message" : "Thanks for providing feedback.  Have a free trade on us!",
    "free" : 1,
    "sentiment" : "Joy"
}
```