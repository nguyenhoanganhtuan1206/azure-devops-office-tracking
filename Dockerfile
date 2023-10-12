FROM gradle:jdk18-focal as builder

WORKDIR /app
COPY . /app

RUN gradle build

FROM gradle:jdk18-focal
WORKDIR /app

COPY --from=builder /app/build/libs/office-tracking-0.0.1-SNAPSHOT.jar /app/app.jar
EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
