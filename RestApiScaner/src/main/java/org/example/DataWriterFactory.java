package org.example;

public interface DataWriterFactory {
    DataWriter createWriter(String format);
}