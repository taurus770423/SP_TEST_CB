package com.demo.exec;

import lombok.Getter;
import lombok.Setter;

public class CommonException extends Exception {
    public CommonException(String message) {
        super(message);
        this.responseMsg = message;
    }

    @Getter
    @Setter
    private String responseMsg = "";
}
