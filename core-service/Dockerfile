FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /workspace
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY common-contracts common-contracts
COPY core-service core-service
COPY realtime-service/pom.xml realtime-service/pom.xml
COPY mcp-server/pom.xml mcp-server/pom.xml
COPY tactical-agent/pom.xml tactical-agent/pom.xml
RUN ./mvnw -B -pl core-service -am package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /workspace/core-service/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
