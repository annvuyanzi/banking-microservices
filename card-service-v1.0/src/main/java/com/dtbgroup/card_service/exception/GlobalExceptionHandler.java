package com.dtbgroup.card_service.exception;

import com.dtbgroup.card_service.dto.response.ApiResponseDto;
import com.dtbgroup.card_service.service.CardService;
import com.dtbgroup.card_service.service.impl.CardServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final  CardServiceImpl cardService;
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ApiResponseDto> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        // Extract all validation error messages from the binding result
        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));
        ApiResponseDto errorResponse = cardService.buildErrorResponse("400", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponseDto> handleIllegalArgument(IllegalArgumentException ex) {
        String message = ex.getMessage() != null ? ex.getMessage() : "Invalid argument";
        ApiResponseDto errorResponse = cardService.buildErrorResponse("400", message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponseDto> handleRuntimeException(RuntimeException ex) {
        String message = ex.getMessage() != null ? ex.getMessage() : "Unexpected error";
        ApiResponseDto errorResponse = cardService.buildErrorResponse("500", message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
