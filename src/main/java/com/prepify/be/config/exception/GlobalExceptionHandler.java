package com.prepify.be.config.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.prepify.be.response.BaseResponse;
import com.prepify.be.response.BaseResponseBuilder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<Object>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.add(errorMessage);
        });

        return ResponseEntity.ok(BaseResponseBuilder.error(400, errors.get(0), null));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<BaseResponse<Object>> handleJsonProcessingException(
            JsonProcessingException ex) {
        if (ex instanceof MismatchedInputException) {
            String fieldName = ((MismatchedInputException) ex).getPath().get(0).getFieldName();
            String errorMessage = fieldName + " có kiểu dữ liệu không hợp lệ.";
            return ResponseEntity.ok(BaseResponseBuilder.error(400, errorMessage, null));
        }

        return ResponseEntity.ok(BaseResponseBuilder.error(400, "Invalid JSON format", null));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MissingServletRequestParameterException.class, HttpMessageNotReadableException.class,
            DataIntegrityViolationException.class, HttpRequestMethodNotSupportedException.class,
            MaxUploadSizeExceededException.class})
    public ResponseEntity<BaseResponse<Object>> handleExceptionParam(Exception ex) {
        if (ex instanceof MissingServletRequestParameterException) {
            String fieldName = ((MissingServletRequestParameterException) ex).getParameterName();
            String errorMessage = fieldName + " là params bắt buộc.";
            return ResponseEntity.ok(BaseResponseBuilder.error(400, errorMessage, null));
        } else if (ex instanceof HttpMessageNotReadableException) {
            String errorMessage = "Request body is not readable";
            return ResponseEntity.ok(BaseResponseBuilder.error(400, errorMessage, null));
        } else if (ex instanceof DataIntegrityViolationException) {
            String errorMessage = "Data integrity violation. This may be due to a duplicate key or a constraint violation.";
            return ResponseEntity.ok(BaseResponseBuilder.error(400, errorMessage, null));
        } else if (ex instanceof HttpRequestMethodNotSupportedException) {
            String errorMessage = ex.getMessage();
            return ResponseEntity.ok(BaseResponseBuilder.error(400, errorMessage, null));
        } else if (ex instanceof MaxUploadSizeExceededException) {
            return ResponseEntity.ok(BaseResponseBuilder.error(400, "Large file size, please choose file <= 25mb", null));
        } else {
            return ResponseEntity.ok(BaseResponseBuilder.error(400, "Bad Request", null));
        }
    }
}

