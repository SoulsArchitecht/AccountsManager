FROM eclipse-temurin:17-jdk-focal as builder

WORKDIR /app

COPY mvnw .
COPY .mvn ./.mvn
COPY pom.xml .
COPY src ./src

RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine

# Установка временной зоны
ENV TZ=Europe/Moscow
RUN apk add --no-cache tzdata && \
    cp /usr/share/zoneinfo/$TZ /etc/localtime && \
    echo $TZ > /etc/timezone

WORKDIR /app

ARG USER_ID=1000
ARG GROUP_ID=1000

RUN addgroup --gid ${GROUP_ID} appgroup && \
    adduser --disabled-password --gecos '' --uid ${USER_ID} --gid ${GROUP_ID} appuser && \
    mkdir -p /app/uploads && \
    chown -R appuser:appgroup /app && \
    chmod -R 755 /app

#RUN apk add --no-cache tzdata

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]