package org.example.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NewsResponse {
    @JsonProperty("items")
    private List<NewsItem> items;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class NewsItem {
        @JsonProperty("title")
        private String title;

        @JsonProperty("link")
        private String url;

        @JsonProperty("pubDate")
        private String date;
        @JsonProperty("description")
        private String description;

        @JsonProperty("thumbnail")
        private String imageUrl;

        public String getTitle() { return title; }
        public String getUrl() { return url; }
        public String getDate() { return date; }
        public String getDescription() { return description; }
        public String getImageUrl() { return imageUrl; }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }
    }

    public void setItems(List<NewsItem> items) {
        this.items = items;
    }

    public List<NewsItem> getNews() {
        return items != null ? items : List.of();
    }
}