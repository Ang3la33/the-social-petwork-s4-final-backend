FROM amazoncorretto:17
WORKDIR /app
COPY target/Social-Petwork-Server-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]