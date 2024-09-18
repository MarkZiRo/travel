package com.swyp6.familytravel.common.exceptionhandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@Order()
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Object> globalException(Exception exception) {
        log.info(exception.toString());
        return ResponseEntity
                .status(500)
                .body(
                        exception.getMessage()
                );
    }
}

