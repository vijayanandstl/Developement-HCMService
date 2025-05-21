# Build stage
FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Run stage
FROM openjdk:17-slim
WORKDIR /app

# Add non-root user
RUN useradd -r -s /bin/false appuser

# Copy the built artifact
COPY --from=build /app/target/*.jar app.jar

# Set environment variables
ENV JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC -XX:MaxGCPauseMillis=200"

# Switch to non-root user
USER appuser

# Health check
HEALTHCHECK --interval=30s --timeout=3s \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"] 