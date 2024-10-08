package com.energy.tajo.global.exception;

import static com.energy.tajo.global.exception.ErrorCode.INVALID_INPUT_VALUE;
import static com.energy.tajo.global.exception.ErrorCode.INVALID_REQUEST;
import static com.energy.tajo.global.exception.ErrorCode.SERVER_ERROR;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import com.fasterxml.jackson.databind.JsonMappingException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EnergyException.class)
    public ResponseEntity<ErrorResponse> handleEnergyException(EnergyException exception) {
        final ErrorCode errorCode = exception.getErrorCode();
        log.warn("{} : {}", errorCode.name(), errorCode.getMessage());
        return ResponseEntity.status(errorCode.getHttpStatus()).body(ErrorResponse.from(errorCode));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolationException(ConstraintViolationException exception) {
        log.warn("ConstraintViolationException: {}", exception.getConstraintViolations());
        return ErrorResponse.from(INVALID_INPUT_VALUE);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        log.warn("MethodArgumentNotValidException: {}",
            exception.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return ErrorResponse.from(INVALID_INPUT_VALUE);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, JsonMappingException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleRequestMappingException(RuntimeException exception) {
        log.warn("Request Mapping Fail : {}", exception.getMessage());
        return ErrorResponse.from(INVALID_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ErrorResponse handleRuntimeException(RuntimeException exception) {
        log.error("UnExpectedException: {}", exception.getMessage());
        return ErrorResponse.from(SERVER_ERROR);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<String> handleMissingRequestHeaderException(MissingRequestHeaderException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("필수 요청 헤더가 누락되었습니다: " + e.getHeaderName());
    }
}