FROM maven:3.6.0-jdk-8-slim AS build
COPY src /home/generator-app/pdf-web-app/src
COPY pom.xml /home/generator-app/pdf-web-app
USER root
RUN --mount=type=cache,target=/.m2 mvn -f /home/generator-app/pdf-web-app/pom.xml clean package

FROM openjdk:8-jre-alpine
COPY --from=build /home/generator-app/pdf-web-app/target/pdf-web-app.jar /usr/local/lib/pdf-web-app.jar
EXPOSE 8080
RUN apk add ttf-dejavu
ENTRYPOINT ["java", "-jar", "/usr/local/lib/pdf-web-app.jar"]