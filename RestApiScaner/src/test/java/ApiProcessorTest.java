

import org.example.APIProcessor;
import org.example.models.NewsResponse.NewsItem;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ApiProcessorTest {

    private final APIProcessor apiProcessor = new APIProcessor();

    @Test
    void processResponse_News_Success() throws IOException {
        String jsonResponse = """
            {
              "items": [
                {
                  "title": "Breaking News",
                  "link": "https://example.com/news/1",
                  "pubDate": "2023-01-01",
                  "description": "Important news",
                  "thumbnail": "https://example.com/image.jpg"
                }
              ]
            }
            """;

        Map<String, Object> result = apiProcessor.processResponse("news", jsonResponse);

        assertEquals("news", result.get("api"));
        assertTrue(result.containsKey("timestamp"));
        @SuppressWarnings("unchecked")
        List<NewsItem> newsItems = (List<NewsItem>) result.get("data");
        assertNotNull(newsItems);
        assertEquals(1, newsItems.size());
        NewsItem item = newsItems.get(0);
        assertEquals("Breaking News", item.getTitle());
        assertEquals("https://example.com/news/1", item.getUrl());
        assertEquals("Important news", item.getDescription());
        assertEquals("https://example.com/image.jpg", item.getImageUrl());
        assertEquals("2023-01-01", item.getDate());
    }

    @Test
    void processResponse_News_InvalidJson_ThrowsIOException() {
        String invalidJson = "not a json";

        assertThrows(IOException.class,
                () -> apiProcessor.processResponse("news", invalidJson));
    }

    @Test
    void processResponse_Financial_ErrorStatus_ReturnsEmptyList() throws IOException {
        String rateLimitJson = """
            {
              "status": { "error_code": 429, "error_message": "rate limit exceeded" }
            }
            """;

        Map<String, Object> result = apiProcessor.processResponse("financial", rateLimitJson);

        assertEquals("financial", result.get("api"));
        @SuppressWarnings("unchecked")
        List<?> data = (List<?>) result.get("data");
        assertNotNull(data);
        assertTrue(data.isEmpty(), "При ошибке rate limit должен вернуться пустой список");
    }

    @Test
    void processResponse_Financial_Success_ReturnsCryptoList() throws IOException {
        String cryptoJson = """
            [
              {
                "id": "bitcoin",
                "symbol": "btc",
                "name": "Bitcoin",
                "current_price": 50000.0,
                "price_change_percentage_24h": 2.5
              },
              {
                "id": "ethereum",
                "symbol": "eth",
                "name": "Ethereum",
                "current_price": 4000.0,
                "price_change_percentage_24h": -1.2
              }
            ]
            """;

        Map<String, Object> result = apiProcessor.processResponse("financial", cryptoJson);

        assertEquals("financial", result.get("api"));
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> data = (List<Map<String, Object>>) result.get("data");
        assertNotNull(data);
        assertEquals(2, data.size());

        Map<String, Object> first = data.get(0);
        assertEquals("btc", first.get("symbol"));
        assertEquals(50000.0, first.get("price"));
        assertEquals(2.5, first.get("change"));

        Map<String, Object> second = data.get(1);
        assertEquals("eth", second.get("symbol"));
        assertEquals(4000.0, second.get("price"));
        assertEquals(-1.2, second.get("change"));
    }

    @Test
    void processResponse_Weather_Success_ReturnsCurrent() throws IOException {
        String weatherJson = """
            {
              "current": {
                "temperature_2m": 15.5,
                "wind_speed_10m": 3.4,
                "time": "2025-06-11T12:00"
              }
            }
            """;

        Map<String, Object> result = apiProcessor.processResponse("weather", weatherJson);

        assertEquals("weather", result.get("api"));
        @SuppressWarnings("unchecked")
        Map<String, Object> data = (Map<String, Object>) result.get("data");
        assertNotNull(data);
        assertEquals(15.5, data.get("temperature"));
        assertEquals(3.4, data.get("windSpeed"));
        assertEquals("2025-06-11T12:00", data.get("time"));
    }

    @Test
    void processResponse_UnknownApi_ReturnsEmptyData() throws IOException {
        Map<String, Object> result = apiProcessor.processResponse("unknownApi", "{}");

        assertEquals("unknownApi", result.get("api"));

        // ожидаем не null, а пустой список
        @SuppressWarnings("unchecked")
        List<?> data = (List<?>) result.get("data");
        assertNotNull(data, "Для несуществующего API data должно быть списком, а не null");
        assertTrue(data.isEmpty(), "Для несуществующего API список должен быть пустым");
    }
}
