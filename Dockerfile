FROM openjdk:24-ea-21-oracle
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
