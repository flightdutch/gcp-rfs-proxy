import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class Backend {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(3000), 0);

        server.createContext("/api/users", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                System.out.println("\n[Java] Отримано запит!");
                System.out.println("[Java] Host заголовок: " + exchange.getRequestHeaders().getFirst("Host"));
                System.out.println("[Java] X-Real-IP (від проксі): " + exchange.getRequestHeaders().getFirst("X-Real-IP"));

                String response = "{\"status\": \"success\", \"message\": \"Привіт від Java бекенду!\"}";

                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, response.getBytes().length);

                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        });

        System.out.println("=== Java додаток стартував і чекає на http://localhost:3000 ===");
        server.start();
    }
}
