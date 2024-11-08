package com.demo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;



public class CurrencyListModel implements Serializable {
    @Setter
    @Getter
    @ToString
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Request implements Serializable {
        private String code;

        private String label;

        @Builder.Default
        private int pageNo = 1;

        @Builder.Default
        private int pageSize = 10;
    }

    @Setter
    @Getter
    @ToString
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Response implements Serializable {
        private int totalPages;

        List<Currency> currencyList;
    }

    @Setter
    @Getter
    @ToString
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Currency implements Serializable {
        private String code;

        private String label;

        @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
        private LocalDateTime createTime;

        @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
        private LocalDateTime modifyTime;
    }
}
