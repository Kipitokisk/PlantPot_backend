# 🏗️ Stage 1: Build Kotlin application
FROM gradle:8.5-jdk17 AS build
WORKDIR /app

# Copy Gradle files and download dependencies (cached)
COPY gradle gradle
COPY gradlew .
COPY build.gradle.kts .
COPY settings.gradle.kts .
RUN ./gradlew dependencies --no-daemon

# Copy the source code
COPY src ./src

# Package the application
RUN ./gradlew clean build -x test --no-daemon

# 🏗️ Stage 2: Run Kotlin application
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copy JAR from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
