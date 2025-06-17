package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(Main.class);

        if (args.length < 4) {
            logger.error("Invalid arguments. Expected usage: " +
                    "<maxThreads> <pollIntervalSec> <outputFormat> <api1> [api2 ...].\n" +
                    "Example: 4 10 json news weather financial");
            return;
        }

        int maxThreads;
        int pollIntervalSec;
        try {
            maxThreads = Integer.parseInt(args[0]);
            pollIntervalSec = Integer.parseInt(args[1]);
            if (maxThreads <= 0 || pollIntervalSec < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            logger.error("Invalid thread or interval value. " +
                    "The first argument (maxThreads) must be > 0, and the second (pollIntervalSec) must be ≥ 0.\n" +
                    "Example: 4 10 json news");
            return;
        }

        String outputFormat = args[2].toLowerCase();
        if (!outputFormat.equals("json") && !outputFormat.equals("csv")) {
            logger.error("Invalid output format: '{}'. Supported formats are 'json' and 'csv'", outputFormat);
            return;
        }

        List<String> apis = Arrays.asList(args).subList(3, args.length);
        if (apis.isEmpty()) {
            logger.error("No APIs provided to poll. Please specify at least one API name as an argument.");
            return;
        }

        Map<String, String> apiUrls = Map.of(
                "news", "https://api.rss2json.com/v1/api.json?rss_url=http://feeds.bbci.co.uk/news/world/rss.xml",
                "financial", "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=10",
                "weather", "https://api.open-meteo.com/v1/forecast?latitude=55.75&longitude=37.61&current=temperature_2m,wind_speed_10m"
        );

        APIClient apiClient = new APIClient();
        APIProcessor processor = new APIProcessor();
        DataWriterFactory writerFactory = format -> format.equals("json")
                ? new JsonDataWriter("output." + format)
                : new CsvDataWriter("output." + format);
        DataWriter writer = writerFactory.createWriter(outputFormat);

        ExecutorService executor = Executors.newFixedThreadPool(maxThreads);
        Semaphore semaphore = new Semaphore(maxThreads);
        List<Future<?>> futures = new ArrayList<>();

        for (String api : apis) {
            String url = apiUrls.get(api);
            if (url == null) {
                logger.warn("Unknown API: {}", api);
                continue;
            }

            Runnable task = () -> {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        semaphore.acquire();
                        try {
                            logger.info("Polling {} API...", api);
                            String response = apiClient.getApiResponse(url);
                            Map<String, Object> data = processor.processResponse(api, response);
                            writer.appendData(data);
                        } finally {
                            semaphore.release();
                        }
                        TimeUnit.SECONDS.sleep(pollIntervalSec);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } catch (IOException e) {
                        // Фатальная ошибка парсинга — выходим из цикла и завершаем задачу
                        logger.error("[{}] Fatal parsing error — stopping polling for this API", api, e);
                        break;
                    }
                }
            };

            futures.add(executor.submit(task));
        }


        // 5. Обработка завершения по Ctrl+C
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Shutdown signal received. Shutting down executor...");
            executor.shutdownNow(); // отправить прерывания задачам
            try {
                if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                    logger.warn("Executor did not terminate in time.");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            logger.info("Shutdown complete.");
        }));

        // 6. Ожидание завершения (если вдруг все задачи сами завершатся — маловероятно)
        try {
            for (Future<?> f : futures) {
                f.get(); // будет блокировать main, пока задачи не завершатся
            }
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Main interrupted or task failed: {}", e.getMessage());
        }
    }
}
