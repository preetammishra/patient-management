
# Stage 1: Build the Spring Boot application
FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /app

# Copy Maven configuration
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build application
RUN mvn clean package -DskipTests


# Stage 2: Run the application
FROM eclipse-temurin:21-jre AS runner

WORKDIR /app

# Copy the generated JAR from builder
COPY --from=builder /app/target/patient-service-0.0.1-SNAPSHOT.jar ./app.jar

# Expose Spring Boot port
EXPOSE 4000

# Start application
ENTRYPOINT ["java", "-jar", "app.jar"]
