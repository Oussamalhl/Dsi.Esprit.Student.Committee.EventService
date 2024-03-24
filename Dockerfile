FROM openjdk:8-jdk-alpine
VOLUME /tmp
EXPOSE 8084
ARG JAR_FILE=target/dsi.esprit.eventservice-1.0-SNAPSHOT.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]