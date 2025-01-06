FROM maven:3.9-eclipse-temurin-23 as build

WORKDIR /build
COPY . .

RUN mvn clean package

FROM eclipse-temurin:23-jre as runtime

WORKDIR /app
COPY --from=build /build/target/*.jar app.jar
CMD ["java", "-jar", "app.jar"]