package com.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CoindeskGetModel implements Serializable {
    private Time time;

    private String disclaimer;

    private String chartName;

    private Map<String, BPI> bpi;

    @Getter
    @Setter
    @ToString
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Time implements Serializable {
        private String updated;

        private String updatedISO;

        @JsonProperty("updateduk")
        private String updatedUK;
    }

    @Getter
    @Setter
    @ToString
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BPI implements Serializable {
        private String code;

        private String symbol;

        private String rate;

        private String description;

        @JsonProperty("rate_float")
        private Float rateFloat;
    }
}
