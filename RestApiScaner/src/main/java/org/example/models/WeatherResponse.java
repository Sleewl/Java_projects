package org.example.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherResponse {
    @JsonProperty("current")
    private CurrentWeather current;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CurrentWeather {
        @JsonProperty("temperature_2m")
        private double temperature2M;
        @JsonProperty("wind_speed_10m")
        private double windSpeed10M;
        @JsonProperty("time")
        private String time;

        public double getWindSpeed() {
            return windSpeed10M;
        }

        public double getTemperature() {
            return temperature2M;
        }

        public String getTime() {
            return time;
        }


        public void setTime(String time) {
            this.time = time;
        }
    }

    public CurrentWeather getCurrent() {
        return current;
    }
}

