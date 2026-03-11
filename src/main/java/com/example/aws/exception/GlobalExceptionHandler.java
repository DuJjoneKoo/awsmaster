package com.example.aws.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice // 모든 Controller의 에러를 여기서 잡음
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class) // 모든 에러가 여기로 들어옴
    public ResponseEntity<?> handleException(Exception e) {

        //에러 내용을 ERROR 레벨로 로그에 남김
        log.error("[API - ERROR] ", e);
        return ResponseEntity
                .status(500)
               .body(e.getMessage());
    }
}