package com.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseModel<T> implements Serializable {
    private T data;

    private String message;

    @Builder.Default
    private StatusEnums status = StatusEnums.SUCCESS;

    public enum StatusEnums {
        SUCCESS, //成功
        FAIL; //失敗
    }
}
