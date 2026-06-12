// Функція для виконання тесту
async function startFrontendClient() {
    console.log("=== [Node.js Frontend] Запуск клієнта ===");
    console.log("Надсилаємо запит на локальний Nginx: http://localhost:8080/api/users\n");

    try {
        // Робимо запит до локального проксі
        const response = await fetch('http://localhost:8080/api/users');

        if (!response.ok) {
            throw new Error(`Помилка сервера: ${response.status}`);
        }

        const data = await response.json();

        console.log("=== [Node.js Frontend] Успіх! Отримано відповідь через Nginx ===");
        console.log(data);

    } catch (error) {
        console.error("❌ [Node.js Frontend] Помилка з'єднання:", error.message);
    }
}

startFrontendClient();
