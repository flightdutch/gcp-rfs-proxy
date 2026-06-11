# Етап збирання (компіляція)
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /app
COPY backend.java .
RUN javac backend.java

# Етап рантайму
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/backend.class .

# Cloud Run за замовчуванням передає порт у змінній оточення PORT (зазвичай 8080)
# Наш Java-код з минулого кроку слухає порт 3000, тому перевизначимо його або змініть 3000 на 8080 у коді Java
EXPOSE 3000

CMD ["java", "backend"]
