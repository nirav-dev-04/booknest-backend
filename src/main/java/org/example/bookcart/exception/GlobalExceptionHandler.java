package org.example.bookcart.exception;

import org.example.bookcart.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // RESOURCE NOT FOUND
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>>
    handleResourceNotFound(

            ResourceNotFoundException ex
    ) {

        ApiResponse<Object> response =
                ApiResponse.builder()

                        .success(false)

                        .message(ex.getMessage())

                        .data(null)

                        .build();

        return new ResponseEntity<>(

                response,

                HttpStatus.NOT_FOUND
        );
    }

    // VALIDATION ERROR
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>>
    handleValidationError(

            MethodArgumentNotValidException ex
    ) {

        String message = ex.getBindingResult()
                .getFieldError()
                .getDefaultMessage();

        ApiResponse<Object> response =
                ApiResponse.builder()

                        .success(false)

                        .message(message)

                        .data(null)

                        .build();

        return new ResponseEntity<>(

                response,

                HttpStatus.BAD_REQUEST
        );
    }

    // GENERAL EXCEPTION
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>>
    handleGeneralException(

            Exception ex
    ) {

        ApiResponse<Object> response =
                ApiResponse.builder()

                        .success(false)

                        .message(ex.getMessage())

                        .data(null)

                        .build();

        return new ResponseEntity<>(

                response,

                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}