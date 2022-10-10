# SpringBoot DynamoDB CRUD App 

### Stack

- Java 15, Springboot 2
- Webflux, Swagger (OpenApi)
- DynamoDB (with pagination), Testcontainers
- Docker (dockerized dynamoDb + UI for it)

### Build

- To build project and docker image: `./gradlew clean build docker`

or

- go to `./scripts` and run `./buildNrun.sh`
- 
### AWS SDK Java v2

- In this repo there is also another branch `aws-sdk-v2`, which contains implementation using AWS SDK Java v2.

### Ports

|  Port  |                Path                    |  Description             |
| ------ | -------------------------------------- | ------------------------ |
|  8080  | http://localhost:8080/api/v1/          | application api          |
|  8080  | http://localhost:8080/                 | Swagger UI               |
|  7001  | http://localhost:7001/                 | dynamo db admin ui       |

### Rest call examples

You can find scripts for testing api in resources folder.