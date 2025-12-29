FROM gradle:9.2-jdk17-alpine AS build
WORKDIR /home/gradle/src
COPY . .
RUN gradle bootJar --no-daemon

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /home/gradle/src/build/libs/*.jar app.jar
EXPOSE 8080
ENV SERVER_PORT=8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
