# Stage 1: Build
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Copy maven wrapper and pom
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw
# Resolve dependencies to cache them in a layer
RUN ./mvnw dependency:go-offline

# Copy source and build
COPY src ./src
RUN ./mvnw clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy the built jar from the build stage
COPY --from=build /app/target/mcp-stdio-server-0.0.1-SNAPSHOT.jar app.jar

# MCP servers using stdio need to disable standard Spring Boot logging to not pollute stdout
ENTRYPOINT ["java", \
            "-Dspring.ai.mcp.server.stdio=true", \
            "-Dspring.main.web-application-type=none", \
            "-Dlogging.pattern.console=", \
            "-jar", "app.jar"]
