FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /workspace
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY common-contracts common-contracts
COPY core-service/pom.xml core-service/pom.xml
COPY realtime-service/pom.xml realtime-service/pom.xml
COPY mcp-server/pom.xml mcp-server/pom.xml
COPY tactical-agent tactical-agent
RUN ./mvnw -B -pl tactical-agent -am package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /workspace/tactical-agent/target/*.jar app.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
