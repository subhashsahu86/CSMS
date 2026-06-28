FROM eclipse-temurin:21-jre

WORKDIR /app/CSMS

COPY target/user-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8083

ENTRYPOINT ["java", "-Duser.timezone=Asia/Kolkata", "-jar", "app.jar"]
