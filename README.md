# Interview app

## Build and run
```
mvn package && java -jar target/accolade-0.1.0.jar
```
if you need to specify data file 
```
mvn package && java -jar target/accolade-0.1.0.jar --dataFile="data3.dat"
```

## Test
```
mvn test
```

## Sample client calls
```
curl localhost:8080
curl -X PATCH -H "Content-Type: application/json" -d @patch.json localhost:8080/record/9957abe2-ab09-41eb-bc70-8f9ac761359c
```

## TODO list
- nicer DAO implementation
- better error handling (esp. for corner cases)
- full-featured patching
- CI build on github (i.e. on Travis)
- more tests
- more docs/comments
- code cleanup (tabs/spaces, organize imports, formatting...)

## Misc Notes
code based on https://spring.io/guides/gs/rest-service/
http tests based on https://spring.io/guides/gs/testing-web/
custom error handling: https://www.baeldung.com/exception-handling-for-rest-with-spring
full-featured patching: https://www.javacodegeeks.com/2018/02/run-away-null-checks-feast-patch-properly-json-patch.html


sample data
```
{"recordId":"6c774739-9676-408b-be42-c7d229c7c3e6","info":{"recordStatus":"UPDATED","created":"2019-01-14T21:19:17.910+0000","updated":["2019-01-14T21:19:41.369+0000","2019-01-14T21:19:43.442+0000","2019-01-14T21:19:46.173+0000"],"deleted":null,"recordData":"3"}}
{"recordId":"39e1220c-98da-4b32-8225-5c1d37255644","info":{"recordStatus":"NEW","created":"2019-01-14T21:19:16.931+0000","updated":[],"deleted":null,"recordData":null}}
{"recordId":"aacf7ad4-7ded-4f3e-ba7b-5a80459d0b52","info":{"recordStatus":"DELETED","created":"2019-01-14T21:19:18.860+0000","updated":[],"deleted":"2019-01-14T21:20:07.979+0000","recordData":null}}
```
