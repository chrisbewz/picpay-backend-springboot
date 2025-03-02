# Picpay Backend Spring Boot Implementation

This project aims to functionally replicate the requisites of the [picpay-backend-challenge](https://github.com/PicPay/picpay-desafio-backend) using spring boot and docker to 
elaborate a functional payments api using mocked resources.

## Table of Contents
- [Technologies Applied](#technologies-applied)
- [Plan](#plan)
- [How to Run](#how-to-run)
    - [Docker Compose](#docker-compose)
    - [Build](#build)
    - [Run](#run)
    - [Test](#test)
- [Support](#support)

## Technologies Applied

- [Spring Framework](https://spring.io/)
  - [Spring Boot](https://spring.io/projects/spring-boot) 
  - [Spring Web]()
  - [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Lombok](https://projectlombok.org/)
- [Docker Compose](https://docs.docker.com/compose/)
- [PostgreSQL](https://www.postgresql.org/)
- [TestContainers](https://testcontainers.com/)

## Plan

> This project is still a work in progress, new features shall be added over time.

Below are registered some goals and additions with respective status to be added.
Issues can be addressed on [Issues](https://github.com/chrisbewz/picpay-backend-springboot/issues) section of this repository.

High-level plan is represented in the table

| Feature                      | Status       |
|------------------------------|--------------|
| Transfers API                | Completed ✔️ |
| Test Containers              | Completed ✔️ |
| Transfer Endpoint Unit Tests | Completed ✔️ |
| Transfers History            | WIP       👷 |
| Transaction Support          | WIP       👷 |

## How to Run

> **Note**: Current project uses maven for dependency management and build tasks, to the following steps shall be based on it as well.
> More documentation about maven can be found at [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
### Docker Compose

To self-host the spring boot api run below command on terminal:

```bash

git clone https://github.com/chrisbewz/picpay-backend-springboot.git
cd docker
docker compose -f ./docker-compose-infrastructure-dev.yml up -d
```
This will configure/start infrastructure services need by api to work properly such as:
- Postgres Transactions Database

### Build

After cloning repo contents to local environment, to build the project the following command must be run on terminal:

```bash
cd repo-root
mvn clean build
```

### Run

```bash
mvn clean package
mvn picpay-backend-application:run

```

### Test

## Support

If you like my work, feel free to:

⭐ this repository.
Thanks a bunch for supporting me!



