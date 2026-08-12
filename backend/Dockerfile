FROM maven:3.9.9-eclipse-temurin-5 AS build
WORKDIR /app
COPY .. .

WORKDIR /app
RUN mvn clean package -DskipTests

FROM eclipse-temurin:25-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]