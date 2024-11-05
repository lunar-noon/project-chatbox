package ch.wiss.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
 
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.validation.ConstraintViolationException;
 

@ControllerAdvice //provides a centralized location for handling exceptions thrown from any controller
public class ControllerAdvisor extends ResponseEntityExceptionHandler {
 //provides custom error handling for validation errors on method arguments
 @Override
 public ResponseEntity<Object> handleMethodArgumentNotValid(
  MethodArgumentNotValidException ex, HttpHeaders headers,
  HttpStatusCode status, WebRequest request) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach((ObjectError error) -> {
    String fieldName = ((FieldError) error).getField();
    String errorMessage;
        errorMessage = error.getDefaultMessage();
    errors.put(fieldName, errorMessage);
 });
 return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
 }
 // handles ConstraintViolationExceptions thrown when validation constraints are violated
 @ExceptionHandler(ConstraintViolationException.class)
 public ResponseEntity<Object> handleNotFoundException(ConstraintViolationException ex) {
 Map<String, String> errors = new HashMap<>();
 errors.put("message", ex.getMessage());
 return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
 }
}