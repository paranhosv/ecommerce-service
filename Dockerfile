FROM openjdk:11 AS BUILD_IMAGE
ENV APP_HOME=/root/dev/application/
RUN mkdir -p $APP_HOME
WORKDIR $APP_HOME
# copy only gradle and download dependencies first to force image layer reuse
COPY build.gradle gradlew $APP_HOME
COPY gradle $APP_HOME/gradle
RUN ./gradlew build -x :bootJar -x test --continue
# copy whole project and run a complete build
COPY . .
RUN ./gradlew build

FROM openjdk:11-jre
WORKDIR /opt/
COPY --from=BUILD_IMAGE /root/dev/application/build/libs/app.jar .
EXPOSE 8080
CMD ["java","-jar","app.jar"]
