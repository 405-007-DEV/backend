FROM openjdk:17-slim AS build

WORKDIR /workspace/app

COPY gradlew .
COPY gradle gradle

RUN chmod +x ./gradlew

COPY build.gradle .
COPY settings.gradle .

COPY src src

RUN ./gradlew build --no-daemon -x test

FROM openjdk:17-slim
WORKDIR /app
COPY --from=build /workspace/app/build/libs/*.jar app.jar

ENTRYPOINT ["java","-jar","/app/app.jar"]