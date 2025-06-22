#FROM eclipse-temurin:17-jdk-jammy
#
#WORKDIR /app
#COPY .mvn/ .mvn
#COPY mvnw pom.xml ./
#
#RUN chmod +x ./mvnw
#
#RUN apt-get update && apt-get install -y dos2unix
#RUN dos2unix ./mvnw
#
#RUN ./mvnw dependency:resolve
#
#COPY src ./src
#
#CMD ["./mvnw", "spring-boot:run"]

FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17.0.1-jdk-slim
COPY --from=build /target/prepify-0.0.1-SNAPSHOT.jar prepify.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","prepify.jar"]