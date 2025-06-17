package org.example;

import java.io.IOException;
import java.util.Map;

public interface DataWriter {
    void appendData(Map<String, Object> data) throws IOException;
}