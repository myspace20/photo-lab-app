# ===== Stage 1: Build =====
FROM maven:3.9.9-eclipse-temurin-21-alpine AS builder
WORKDIR /workspace
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# ===== Stage 2: Run =====
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY --from=builder /workspace/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
