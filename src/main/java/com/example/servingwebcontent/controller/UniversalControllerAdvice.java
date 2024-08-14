package com.example.servingwebcontent.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class UniversalControllerAdvice {

    @ExceptionHandler(Exception.class)
    public void handleException(Exception e) {
        log.error(e.getMessage());
    }

}
