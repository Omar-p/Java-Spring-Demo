FROM eclipse-temurin:17.0.6_10-jre-alpine

ARG JAR_FILE=target/*SNAPSHOT.jar

COPY ${JAR_FILE} app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]