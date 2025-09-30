package com.pratham.shortner.advice;

import com.pratham.shortner.exception.AliasAlreadyExistsException;
import com.pratham.shortner.exception.InvalidUrlException;
import com.pratham.shortner.exception.NotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AliasAlreadyExistsException.class)
    public ResponseEntity<Object> handleAliasConflict(AliasAlreadyExistsException ex) {
        Map<String, String> body = new HashMap<>();
        body.put("error", "alias_conflict");
        body.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(InvalidUrlException.class)
    public ResponseEntity<Object> handleInvalidUrl(InvalidUrlException ex) {
        Map<String, String> body = new HashMap<>();
        body.put("error", "invalid_url");
        body.put("message", ex.getMessage());
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> body = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(err -> body.put(err.getField(), err.getDefaultMessage()));
        return new ResponseEntity<>(body, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFound(NotFoundException ex) {
        Map<String, String> body = new HashMap<>();
        body.put("error", "not_found");
        body.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

}
