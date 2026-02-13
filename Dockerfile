FROM maven:4.0.0-rc-5-amazoncorretto-25 AS build
LABEL authors="ASKekishev and LAGuryanov"
WORKDIR /app
COPY . .

RUN mvn clean package

FROM amazoncorretto:25-headless
WORKDIR /app

COPY --from=build /app/target/musicbot-1.0-SNAPSHOT.jar /app.jar

EXPOSE 2007

ENTRYPOINT ["java", "-jar", "/app.jar"]