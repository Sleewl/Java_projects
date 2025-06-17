package org.example.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CryptoResponse {
    public String id;
    public String symbol;
    public String name;
    @JsonProperty("current_price")
    public double price;
    @JsonProperty("price_change_percentage_24h")
    public double change24h;

    public String getId() {
        return id;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public double getChange24h() {
        return change24h;
    }
}