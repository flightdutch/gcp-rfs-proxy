# gcp-rfs-proxy
gcp - test solution: frontend -> proxy -> backend


Infrastructura: How it works

[Front-end (curl/node.js)]
           │
           ▼  (HTTPS запит на порт 443)
┌────────────────────────────────────────────────────────┐
│ Google Cloud Run (Шлюз)                                │
└──────────┬─────────────────────────────────────────────┘
           │
           ▼  (Прокидає трафік всередину контейнера на порт 8080)
┌────────────────────────────────────────────────────────┐
│ Контейнер #1: Nginx (Proxy)                             │
│ - Обробляє роут /api/                                  │
│ - Підставляє SNI (proxy_ssl_server_name on)            │
│ - Переписує Host заголовок                             │
└──────────┬─────────────────────────────────────────────┘
           │
           ▼  (Новий HTTPS запит у хмарі)
┌────────────────────────────────────────────────────────┐
│ Контейнер #2: Java Backend                             │
│ - Слухає динамічний $PORT від GCP                      │
│ - Завантажує анонімні класи (Backend$1.class)          │
│ - Обробляє /api/users та повертає JSON                 │
└────────────────────────────────────────────────────────┘

Service-account - build/upload/start Run Functions (only for test - not production):

    Artifact Registry Administrator
    Cloud Build Editor
    Cloud Datastore User
    Cloud Functions Developer
    Cloud Run Developer
    Firebase Admin
    Firebase Cloud Messaging Admin
    Service Account User
    Storage Admin

Artifact Registry - config by default:
   backend-repo
   proxy-repo

Storage bucket -

Фронтенд: Тепер ви можете спокійно запускати ваш локальний Node.js-клієнт (node client.js), адресу проксі:
https://[SERVICE_NAME]-[PROJECT_ID_HASH]-[REGION_CODE].a.run.app.
Adress take from Action workflow log: ${{ steps.deploy.outputs.url }}
As example:
https://java-backend-api-3eetralyalyakkirq-ey.a.run.app

Розширення логіки: Якщо ви додасте нові ендпоінти в Java (наприклад, /api/products чи /api/auth), вам не потрібно переналаштовувати Nginx. Він автоматично проксіюватиме все, що починається з /api/.

Безпека: Ваш Java-бекенд тепер повністю схований за проксі. У майбутньому його можна буде взагалі закрити від зовнішнього світу (зробити Ingress: Internal), щоб до нього мав доступ тільки ваш Nginx.

GCP Cloud Shell - T-shoot/Check:
Test backend:
curl https://java-backend-api-[PROJECT_ID_HASH].a.run.app/api/users

Log CloudBuild backend:
gcloud logging read "resource.type=cloud_run_revision AND resource.labels.service_name=java-backend-api" --limit=20 --format="value(textPayload)"

Check all infrastructure - proxy > backend
curl https://nginx-proxy-api-[PROJECT_ID_HASH].a.run.app/api/users
