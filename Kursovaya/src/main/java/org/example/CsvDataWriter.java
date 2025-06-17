package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class CsvDataWriter implements DataWriter {
    private static final ObjectMapper jsonMapper = new ObjectMapper();
    private final File outputFile;
    private final CsvSchema schema;
    private boolean isFirstWrite = true;

    public CsvDataWriter(String filename) {
        this.outputFile = new File(filename);
        this.schema = CsvSchema.builder()
                .addColumn("api")
                .addColumn("timestamp")
                .addColumn("data")
                .build()
                .withHeader();
    }

    public synchronized void appendData(Map<String, Object> data) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(outputFile, true))) {
            String jsonData = jsonMapper.writeValueAsString(data.get("data"));
            String csvLine = String.format("\"%s\",%d,\"%s\"",
                    data.get("api"),
                    data.get("timestamp"),
                    jsonData.replace("\"", "\"\""));

            if (isFirstWrite) {
                writer.println("api,timestamp,data");
                isFirstWrite = false;
            }

            writer.println(csvLine);
        } catch (JsonProcessingException e) {
            throw new IOException("Failed to serialize data to JSON", e);
        }
    }
}