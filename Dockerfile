# =========================================================================
# Stage 1: Build the application using Maven and a full JDK
# =========================================================================
FROM maven:3.8.5-openjdk-17 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml file to download dependencies first, leveraging Docker's cache
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the rest of the source code
COPY src ./src

# Package the application, skipping tests. This creates the JAR file.
RUN mvn clean package -DskipTests


# =========================================================================
# Stage 2: Create the final, lightweight production image
# =========================================================================
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the executable JAR file from the 'build' stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port the application runs on
EXPOSE 8080

# The command to run the application when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]