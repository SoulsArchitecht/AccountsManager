FROM eclipse-temurin:17-jdk-focal as builder

WORKDIR /app

COPY mvnw .
COPY .mvn ./.mvn
COPY pom.xml .
COPY src ./src

RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine

ENV TZ=Europe/Moscow
RUN apk add --no-cache tzdata && \
    cp /usr/share/zoneinfo/$TZ /etc/localtime && \
    echo $TZ > /etc/timezone

WORKDIR /app

# Передаём UID и GID как аргументы сборки
ARG USER_ID=1000
ARG GROUP_ID=1000

# Создаём группу с заданным GID
RUN addgroup -g ${GROUP_ID} appgroup && \
#     Создаём пользователя: указываем UID, группу, без home, без пароля
    adduser -u ${USER_ID} -G appgroup -D -S appuser && \
#     Создаём папку для загрузок
    mkdir -p /app/uploads && \
#     Даём права пользователю
    chown -R appuser:appgroup /app && \
    chmod -R 755 /app

COPY --from=builder /app/target/*.jar app.jar

USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]