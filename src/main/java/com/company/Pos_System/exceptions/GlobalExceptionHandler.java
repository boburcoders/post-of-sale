package com.company.Pos_System.exceptions;

import com.company.Pos_System.dto.ErrorDto;
import com.company.Pos_System.dto.HttpApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<HttpApiResponse<ErrorDto>> error_404(ItemNotFoundException e, HttpServletRequest request) {
        ErrorDto errorDto = ErrorDto.builder()
                .errorCode(HttpStatus.NOT_FOUND.value())
                .errorPath(request.getRequestURI())
                .errorBody(e.getMessage())
                .build();
        HttpApiResponse<ErrorDto> response = HttpApiResponse.<ErrorDto>builder()
                .success(false)
                .status(HttpStatus.NOT_FOUND)
                .message("Item not found")
                .data(errorDto)
                .build();

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<HttpApiResponse<ErrorDto>> notValidExceptionHandler(MethodArgumentNotValidException e, HttpServletRequest request) {
        Map<String, List<String>> errorBody = new HashMap<>();
        for (FieldError fieldError : e.getFieldErrors()) {
            String field = fieldError.getField();
            String message = fieldError.getDefaultMessage();
            errorBody.compute(field, (s, strings) -> {
                strings = Objects.requireNonNullElse(strings, new ArrayList<>());
                strings.add(message);
                return strings;
            });
        }
        ErrorDto errorDto = ErrorDto.builder()
                .errorCode(HttpStatus.NOT_ACCEPTABLE.value())
                .errorPath(request.getRequestURI())
                .errorBody(errorBody)
                .build();
        HttpApiResponse<ErrorDto> response = HttpApiResponse.<ErrorDto>builder()
                .success(false)
                .status(HttpStatus.BAD_REQUEST)
                .message("Invalid request")
                .data(errorDto)
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
