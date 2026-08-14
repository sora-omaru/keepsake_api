package com.omaru.keepsake_api.exception;

import com.omaru.keepsake_api.dto.response.ErrorResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponseDto> handleApiException(ApiException exception) {
        ErrorResponseDto response = new ErrorResponseDto(
                exception.getStatus().value(),
                exception.getStatus().getReasonPhrase(),
                exception.getMessage(),
                OffsetDateTime.now()
        );

        return ResponseEntity.status(exception.getStatus()).body(response);
    }
}
