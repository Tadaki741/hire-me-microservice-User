Run `docker-compose up` to spin up the all with database

-----
Step to build Docker with Spring Boot. This is only for documentation of the steps involved in spinning a Spring Boot app in Docker container. You don't have to follow them.

1. Create JAR file
`mvn package`
2. Containerize the spring boot app in a `Dockerfile`
3. Build the image `docker build -t hire-me-microservice-user .`
4. Create a `docker-compose.yml` file as the app depends on a database
5. Run `docker-compose up --build` to spin up postgres db and spring boot app

Note: if changes are made to the app, you need to rebuild the image.

```
mvn clean && mvn package
docker-compose up --build
```

`mvn clean && mvn package` is to clean and rebuild jar file

`docker-compose up --build` is to rebuild the docker image

# How to dev on local

Go to `application.properties`
- Change `spring.datasource.url=jdbc:postgresql://localhost:25432/microservice-user`

Go to `application.yml`
- Change `spring.kafka.consumer.bootstrap-servers` to `localhost:19092`
- Change `spring.kafka.producer.bootstrap-servers` to `localhost:19092`
- Change `spring.redis.host` to `localhost`
