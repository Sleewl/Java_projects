
import org.example.APIClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.sun.net.httpserver.HttpServer;


import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import static org.junit.jupiter.api.Assertions.*;

class APIClientTest {
    private HttpServer server;
    private String baseUrl;

    @BeforeEach
    void startServer() throws IOException {
        // Запускаем HTTP-сервер на случайном порту
        server = HttpServer.create(new InetSocketAddress(0), 0);

        // Контекст /success: возвращает 200 и "OK!"
        server.createContext("/success", exchange -> {
            byte[] resp = "OK!".getBytes();
            exchange.sendResponseHeaders(200, resp.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(resp);
            }
            exchange.close();
        });

        // Контекст /error: сразу закрывает соединение без ответа
        server.createContext("/error", exchange -> {
            // не вызываем sendResponseHeaders — клиент получит IOException
            exchange.getResponseBody().close();
            exchange.close();
        });

        server.start();
        int port = server.getAddress().getPort();
        baseUrl = "http://localhost:" + port;
    }

    @AfterEach
    void stopServer() {
        server.stop(0);
    }

    @Test
    void testGetApiResponse_Success() throws IOException, InterruptedException {
        APIClient client = new APIClient();
        String response = client.getApiResponse(baseUrl + "/success");
        assertEquals("OK!", response);
    }

    @Test
    void testGetApiResponse_ServerError() {
        APIClient client = new APIClient();
        assertThrows(IOException.class, () -> {
            client.getApiResponse(baseUrl + "/error");
        });
    }

    @Test
    void testGetApiResponse_InvalidUrl() {
        APIClient client = new APIClient();
        assertThrows(IOException.class, () -> {
            client.getApiResponse("http://nonexistent.localhost");
        });
    }
}
