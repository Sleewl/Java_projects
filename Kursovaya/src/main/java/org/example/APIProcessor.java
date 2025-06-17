package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.*;
import java.util.Collections;

public class APIProcessor {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(APIProcessor.class);

    public Map<String, Object> processResponse(String apiName, String response) throws IOException {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("api", apiName);
        result.put("timestamp", System.currentTimeMillis());

        try {
            switch (apiName.toLowerCase()) {
                case "news":
                    NewsResponse news = mapper.readValue(response, NewsResponse.class);
                    result.put("data", news.getNews());
                    break;

                case "financial":
                    // Сначала читаем в дерево, чтобы обработать статус rate limit
                    JsonNode root = mapper.readTree(response);
                    if (root.has("status") && root.get("status").has("error_code")) {
                        int code = root.get("status").get("error_code").asInt();
                        String msg = root.get("status").get("error_message").asText();
                        // Предупреждаем, но не бросаем исключение
                        logger.warn("[financial] API returned error {}: {}", code, msg);
                        result.put("data", Collections.emptyList());
                        break;
                    }
                    // Если статус отсутствует, парсим массив криптовалют
                    CryptoResponse[] cryptoArray = mapper.treeToValue(root, CryptoResponse[].class);
                    List<Map<String, Object>> cryptoData = new ArrayList<>();
                    for (CryptoResponse crypto : cryptoArray) {
                        Map<String, Object> cryptoMap = new LinkedHashMap<>();
                        cryptoMap.put("symbol", crypto.getSymbol());
                        cryptoMap.put("price", crypto.getPrice());
                        cryptoMap.put("change", crypto.getChange24h());
                        cryptoData.add(cryptoMap);
                    }
                    result.put("data", cryptoData);
                    break;

                case "weather":
                    WeatherResponse weather = mapper.readValue(response, WeatherResponse.class);
                    Map<String, Object> weatherData = new LinkedHashMap<>();
                    weatherData.put("temperature", weather.getCurrent().getTemperature());
                    weatherData.put("windSpeed", weather.getCurrent().getWindSpeed());
                    weatherData.put("time", weather.getCurrent().getTime());
                    result.put("data", weatherData);
                    break;

                default:
                    result.put("data", Collections.emptyList());
                    break;
            }
        } catch (JsonProcessingException e) {
            throw new IOException("Failed to process " + apiName + " API response", e);
        }

        return result;
    }
}

