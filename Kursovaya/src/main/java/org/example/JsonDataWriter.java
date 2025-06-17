package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JsonDataWriter implements DataWriter {
    private static final ObjectMapper mapper = new ObjectMapper();
    private final File outputFile;

    public JsonDataWriter(String filename) {
        this.outputFile = new File(filename);
    }

    public synchronized void appendData(Map<String, Object> data) throws IOException {
        List<Map<String, Object>> records = getExistingRecords();
        records.add(data);
        mapper.writerWithDefaultPrettyPrinter().writeValue(outputFile, records);
    }

    public ArrayList<Map<String, Object>> getExistingRecords() throws IOException {
        if (outputFile.exists() && outputFile.length() > 0) {
            return mapper.readValue(outputFile, new TypeReference<>() {
            });
        }
        return new ArrayList<>();
    }
}