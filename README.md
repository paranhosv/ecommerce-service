# Ecommerce Demo Service
This is a demo service built with Java 11 for an ecommerce domain context.

It is built with SpringBoot and Docker in order to speed up the development.

### Disclaimer
This project does not care about the precision of discount of products, since it
is using float (and not BigDecimal) for math operations and assuming it is not
the purpose on the project.

### How do I run the project?

#### Requirements
- docker
- docker-compose

### Configurations
The configurations are flexible and can be changed in env vars (by default)
or application.properties:
- DT_BLACK_FRIDAY: Date or dates for Black Friday in format YYYY-MM-DD comma separated.
- DISCOUNT_SERVICE_HOSTNAME: Discount service address in network;
- DISCOUNT_SERVICE_PORT: Discount service port accepting requests;

Examples:
```
DT_BLACK_FRIDAY=2021-11-26
DISCOUNT_SERVICE_HOSTNAME=localhost
DISCOUNT_SERVICE_PORT=50051
```

```
DT_BLACK_FRIDAY=2021-11-26, 2022-11-25
DISCOUNT_SERVICE_HOSTNAME=discount-service
DISCOUNT_SERVICE_PORT=50051
```

#### Running the project
Clone this repository and run the following commands on project root path:
```bash
$ docker build -t ecommerce-service .
$ docker-compose up
```
Wait for the containers be ready and open your browser
on http://localhost:8080/swagger-ui/index.html to read
and use API documentation.
    
