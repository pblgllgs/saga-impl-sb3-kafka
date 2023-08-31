# SAGA Design Pattern

Kafka and spring boot


## Up mysql db

```
docker compose up -d
```

## Add items

```
http://localhost:8082/api/addItems
```

then test

```
http://localhost:8080/api/orders
```

```
{
    "item":"books",
    "quantity":"10",
    "amount":"1000",
    "address": "Bombay",
    "paymentMethod":"Credit card"
}
```