FROM openjdk:17-jdk-slim

MAINTAINER shohag

EXPOSE 8080

RUN apt-get update

COPY target/repotracker.jar repotracker.jar

ENTRYPOINT ["java", "-jar", "repotracker.jar"]