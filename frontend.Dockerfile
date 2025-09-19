FROM node:18-alpine

WORKDIR /app

# Установка зависимостей
COPY ui/package*.json ./
RUN npm install

# Копируем весь frontend
COPY ui .

# Собираем продакшен-версию
RUN npm run build

# Сервим через serve
RUN npm install -g serve
EXPOSE 3000

CMD ["serve", "-s", "build", "-p", "3000"]