package com.openwt.officetracking.api;

import com.openwt.officetracking.error.DomainException;
import com.openwt.officetracking.error.ErrorDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static java.time.Instant.now;

@RestControllerAdvice
public class ControllerErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({DomainException.class})
    public ResponseEntity<ErrorDTO> handleDomainError(final DomainException error) {
        final var errorDTO = ErrorDTO.builder()
                .message(error.getMessage())
                .occurAt(now())
                .build();

        return ResponseEntity
                .status(error.getHttpStatus())
                .body(errorDTO);
    }
}
