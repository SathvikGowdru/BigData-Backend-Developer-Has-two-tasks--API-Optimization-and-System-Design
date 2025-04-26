
# Pick-a-Spot

Spring Boot service to pick the best slot for a container.

## How to Run
```bash
mvn spring-boot:run
```

## Sample Request
```bash
curl -X POST http://localhost:8080/pickSpot -H "Content-Type: application/json" -d '{
  "container": {
    "id": "C1",
    "size": "small",
    "needsCold": false,
    "x": 0,
    "y": 0
  },
  "yardMap": [
    { "x": 1, "y": 1, "sizeCap": "small", "hasColdUnit": false, "occupied": false },
    { "x": 2, "y": 2, "sizeCap": "big",   "hasColdUnit": true,  "occupied": false }
  ]
}'
```

## Sample Response
```json
{ "containerId": "C1", "targetX": 1, "targetY": 1 }
```
