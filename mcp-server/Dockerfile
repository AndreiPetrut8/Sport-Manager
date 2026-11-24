FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /workspace
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY common-contracts common-contracts
COPY core-service/pom.xml core-service/pom.xml
COPY realtime-service/pom.xml realtime-service/pom.xml
COPY mcp-server mcp-server
COPY tactical-agent/pom.xml tactical-agent/pom.xml
RUN ./mvnw -B -pl mcp-server -am package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /workspace/mcp-server/target/*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
