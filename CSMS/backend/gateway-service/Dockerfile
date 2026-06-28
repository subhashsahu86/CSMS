FROM eclipse-temurin:21-jre

WORKDIR /app/CSMS

COPY ./target/gateway-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Duser.timezone=Asia/Kolkata", "-jar", "app.jar"]
