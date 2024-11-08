package com.demo.controller.handler;

import com.demo.exec.CommonException;
import com.demo.model.ResponseModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CommonException.class)
    public ResponseEntity<ResponseModel<Boolean>> handleCommonException(CommonException ex) {
        ResponseModel<Boolean> response = ResponseModel.<Boolean>builder()
                .data(false)
                .message(ex.getResponseMsg())
                .status(ResponseModel.StatusEnums.FAIL)
                .build();
        return ResponseEntity.ok(response);
    }
}