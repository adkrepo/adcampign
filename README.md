# adcampign
1)Required Java1.7,apache-maven-3.3.9

2)Clone AdCampign project from Github

3)Open command prompt , from AdCampign project directory and Run "mvn clean install" (compile , run unit tests and create war file)

4)Once Build success from AdCampign project directory Run "mvn jetty:run"(Starts Jetty server on Port 8080 and deploy AdCampign war)

5)Once server started from AdCampign project directory  Run "mvn failsafe:integration-test"(Run the integration tests)


Rest service endpoints to validate from POSTMAN:

1)Create Ad (POST Method):: http://localhost:8080/ad/

Request Header:
Content-Type: application/json
Accept: application/json

Request Body:
{
"partner_id":"100",
"duration":"60",
"ad_content":"Thanksgiving Sale Ad"
}

2)Get Ad Content for specific partner (GET Method):: http://localhost:8080/ad/{partner_id}

Request Header:
Content-Type: application/json
Accept: application/json

3)Get All Ads Content (GET Method):: http://localhost:8080/ad/all

Request Header:
Content-Type: application/json
Accept: application/json
