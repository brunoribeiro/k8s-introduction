FROM maven:3.8.4-openjdk-8-slim as build
WORKDIR /app
COPY . /app
RUN mvn clean package

FROM openjdk:11-jre-slim-bullseye
COPY --from=build /app/target/weather-api-0.0.1-SNAPSHOT.jar /weather-api-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java","-jar","/weather-api-0.0.1-SNAPSHOT.jar"]