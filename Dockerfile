# Build stage
FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app

# Copy pom.xml first for better layer caching
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Create a non-root user
RUN groupadd -r spring && useradd -r -g spring spring

# Copy the built jar from build stage
COPY --from=build /app/target/candidate-microservice-1.0.0.jar app.jar

# Set proper permissions
RUN chown -R spring:spring /app

# Switch to non-root user
USER spring

# Health check
HEALTHCHECK --interval=30s --timeout=3s \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# Expose port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"] 