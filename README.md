code based on https://spring.io/guides/gs/rest-service/
http tests based on https://spring.io/guides/gs/testing-web/


Build: mvn package && java -jar target/accolade-0.1.0.jar
curl localhost:8080
curl -X PATCH -H "Content-Type: application/json" -d @patch.json localhost:8080/record/9957abe2-ab09-41eb-bc70-8f9ac761359c

custom error handling: https://www.baeldung.com/exception-handling-for-rest-with-spring
full-featured patching: https://www.javacodegeeks.com/2018/02/run-away-null-checks-feast-patch-properly-json-patch.html