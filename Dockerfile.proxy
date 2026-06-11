FROM nginx:alpine

# Копіюємо наш конфіг всередину контейнера Nginx
COPY nginx.conf /etc/nginx/nginx.conf

# Відкриваємо порт 8080
EXPOSE 8080

CMD ["nginx", "-g", "daemon off;"]
