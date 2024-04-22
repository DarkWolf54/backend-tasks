package com.backend.task.infrastructure.rest.advice;

import com.backend.task.infrastructure.adapter.exception.TaskException;
import com.backend.task.infrastructure.rest.response.ErrorResponse;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice(annotations = RestController.class)
public class ControllerAdvice {

    private static final Logger log = LogManager.getLogger(ControllerAdvice.class);

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException illegalArgumentException){
        log.error(illegalArgumentException.getMessage());
        Map<String, String> errors = new HashMap<>();
        errors.put("Error: ", illegalArgumentException.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(errors, HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.toString());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(TaskException.class)
    public ResponseEntity<ErrorResponse> handleTaskException(TaskException taskException){
        log.error(taskException.getErrorMessage());
        Map<String, String> errors = new HashMap<>();
        errors.put("Error: ", taskException.getErrorMessage());
        ErrorResponse errorResponse = new ErrorResponse(errors, HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.toString());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleInvalidArguments(MethodArgumentNotValidException exception){
        log.error("Uno o mas campos de entrada no tienen el formato esperado.");
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        ErrorResponse errorResponse = new ErrorResponse(errors, HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.toString());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
