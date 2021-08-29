## 

#Dependencies

This project has been developed using the next stack:
- Java 11
- Micronaut 3.0
- MongoDB
- Gradle
- Docker
- Docker-compose

The project is package using docker so you can build/run this if you have installed docker-compose

#Build and run

- You can build the project using gradle: 
    ./gradlew clean build
  
- You can build using docker too:
    docker-compose build

#API specification

 - Create User:
 
    `POST /user --data {"user":"<user-id>"}`    
 
- Create contact:

  `POST /contact --data {"userId":"<user-id>","contact":{"phoneNumber":"<phone-number>","name":"<contact-name>"}}`

 - Update contact:

  `PATCH /contact --data {"phoneNumber":"<phone-number>","name":"<contact-name>"}`

 - Unassing contact:

  `PATCH /contact/unassign --data {"userId":"<user-id>","contact":{"phoneNumber":"<phone-number>","name":"<contact-name>"}}`

 - Unassing contact:

  `GET /contact/duplicated?user=<userId>&userToCompare=<userId> --data {"userId":"<user-id>","contact":{"phoneNumber":"<phone-number>","name":"<contact-name>"}}`

 - Recovery contact by user:

  `GET /user?user=<userId> --data {"userId":"<user-id>","contact":{"phoneNumber":"<phone-number>","name":"<contact-name>"}}`

## Commands to invoke API

curl --location --request POST 'localhost:8080/user' \
--header 'Content-Type: application/json' \
--data-raw '{"user":"u1112"}'


curl --location --request POST 'localhost:8080/contact' \
--header 'Content-Type: application/json' \
--data-raw '{"userId":"u1112","contact":{"phoneNumber":"651667516","name":"BBB"}}'

curl --location --request POST 'localhost:8080/contact' \
--header 'Content-Type: application/json' \
--data-raw '{"userId":"u1112","contact":{"phoneNumber":"651667515","name":"BBB"}}'

curl --location --request POST 'localhost:8080/user' \
--header 'Content-Type: application/json' \
--data-raw '{"user":"u1113"}'

curl --location --request POST 'localhost:8080/contact' \
--header 'Content-Type: application/json' \
--data-raw '{"userId":"u1113","contact":{"phoneNumber":"651667516","name":"BBB"}}'


curl --location --request GET 'localhost:8080/contact/duplicated?user=u1113&userToCompare=u1112'

curl --location --request GET 'localhost:8080/contact/?user=u1113'