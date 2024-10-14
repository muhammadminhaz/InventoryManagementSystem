# Use Maven to build the project
FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Use an official OpenJDK runtime for the final image
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copy the jar file from the Maven build stage
COPY --from=build /app/target/InventoryManagementSystem-0.0.1-SNAPSHOT.jar /app/app.jar

# Expose the port the Spring Boot app runs on
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]
