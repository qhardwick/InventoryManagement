package com.skillstorm.exceptions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static Environment environment;

    @Autowired
    public GlobalExceptionHandler(Environment environment) {
        this.environment = environment;
    }

    // Item not found:
    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleItemNotFoundException(ItemNotFoundException exception) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setCode(HttpStatus.NOT_FOUND.value());
        errorMessage.setMessage(exception.getMessage());

        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    // Warehouse not found:
    @ExceptionHandler(WarehouseNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleWarehouseNotFoundException(WarehouseNotFoundException exception) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setCode(HttpStatus.NOT_FOUND.value());
        errorMessage.setMessage(exception.getMessage());

        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    // Handles Exceptions thrown from Validation failures as defined in the DTOs:
    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class})
    public ResponseEntity<ErrorMessage> handleValidationExceptions(MethodArgumentNotValidException e) {
        ErrorMessage error = new ErrorMessage();
        error.setCode(HttpStatus.BAD_REQUEST.value());
        //error.setMessage(e.getBindingResult().getAllErrors().stream().map(ObjectError::getDefaultMessage)
        //        .collect(Collectors.joining(", ")));

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


}
