FROM maven:3.8.3-openjdk-17-slim AS dependencies
WORKDIR /home/app
COPY pom.xml pom.xml
RUN mvn dependency:resolve dependency:resolve-plugins

FROM dependencies AS build
COPY . .
RUN mvn package -DskipTests

FROM eclipse-temurin:17-jre AS deploy
WORKDIR /home/app
COPY --from=build /home/app/target/product-example-0.0.1-SNAPSHOT.jar /home/app/product-example-0.0.1-SNAPSHOT.jar
COPY ./src/main/resources /home/app/back/src/main/resources
EXPOSE 9500
ENTRYPOINT ["java","-jar","/home/app/product-example-0.0.1-SNAPSHOT.jar"]
