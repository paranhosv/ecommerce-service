# Ecommerce Demo Service
This is a demo service built with Java 11 for an ecommerce domain context.

It is built with SpringBoot and Docker in order to speed up the development.

### How do I run the project?

#### Requirements
- docker
- docker-compose

#### Running the project
Clone this repository and run the following commands on project root path:
```bash
$ docker build -t ecommerce-service .
$ docker-compose up
```
Wait for the containers be ready and open your browser
on http://localhost:8080/swagger-ui/index.html to read
and use API documentation.
    
