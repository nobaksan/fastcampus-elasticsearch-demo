package com.example.demo.controller.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class CarMaster {

    @Getter
    @Setter
    public static class Request {
        private String indexName;
        private int page;
        private int size;
        private String keyword;
    }

    @Getter
    @Setter
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
    public static class Response {

        private String imageUrl;
        private String brand;
        private String model;
        private Long price;
        private Long odometer;
        private Integer year;
        private String color;

        /*
        private String regionUrl;
        private String fuel;
        private String type;
        private String manufacturer;
        private String transmission;
        private String vin;
        private String id;
        private String postingDate;
        private String cylinders;
        private String url;
        @JsonProperty("timeStamp")
        private String timeStamp;
        private String condition;
        private String size;
        private String drive;
        private String titleStatus;

        private Area area;
        private List<Double> location;
        @Getter
        @Setter
        public static class Area {
            private String country;
            private String state;
            private String region;
        }
         */
    }

    @Getter
    @Setter
    public static class CompletionRequest {
        private String indexName;
        private String keyword;
    }

    @Getter
    @Setter
    @Builder
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
    public static class CompletionResponse {
        private String id;
        private Integer year;
        private String manufacturer;
        private String transmission;
        private String model;
        private String brand;
    }
}
