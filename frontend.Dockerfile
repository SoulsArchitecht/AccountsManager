FROM node:18-alpine AS builder
WORKDIR /app
COPY ui/package*.json ./
RUN npm ci --silent
COPY ui/ui ./ui
RUN npm run build --prefix ui

FROM nginx:alpine
COPY --from=builder /app/ui/build /usr/share/nginx/html
COPY nginx/conf.d/app.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]