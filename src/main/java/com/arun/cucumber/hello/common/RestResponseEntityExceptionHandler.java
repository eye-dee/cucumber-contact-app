package com.arun.cucumber.hello.common;

import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.StaleObjectStateException;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);

    Pattern UK_PATTERN = Pattern.compile("constraint \\[(?<constraint>.+)\\]");

    @ExceptionHandler(value = {javax.validation.ConstraintViolationException.class})
    protected ResponseEntity<Object> handleIntegrityConstraint(javax.validation.ConstraintViolationException ex,
                                                               WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        String[] message = ex.getMessage().split(":");
        errors.put(message[0].split("\\.")[2], message[1].trim());
        Error error = new Error(errors);
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(value = {SQLIntegrityConstraintViolationException.class, ConstraintViolationException.class})
    protected ResponseEntity<Object> handleIntegrityConstraint(RuntimeException ex, WebRequest request) {

        Matcher matcher = this.UK_PATTERN.matcher(ex.getMessage());
        matcher.find();
        String constraint = matcher.group("constraint");
        String message = "Two contacts cannot have the same " + getFieldName(constraint) + ".";

        Map<String, String> errors = new HashMap<>();
        errors.put(constraint.split("_")[2], message);

        Error error = new Error(errors);

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {StaleObjectStateException.class})
    protected ResponseEntity<Map<String, String>> optimisticLockingFailureException(StaleObjectStateException ex,
                                                                                    WebRequest request) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("version", "this info was update by other action. Please refresh");
        return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
    }

    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        Error error = new Error(errors);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {IllegalStateException.class})
    protected ResponseEntity<Map<String, String>> handleIllegalStateException(IllegalStateException ex,
                                                                              WebRequest request) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("version", "this info was update by other action. Please refresh");
        return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {

        Map<String, String> errors = new HashMap<>();

        if (ex.getCause() instanceof InvalidFormatException) {

            InvalidFormatException exception = (InvalidFormatException) ex.getCause();
            List<Reference> path = exception.getPath();

            path.forEach((error) -> {
                String fieldName = error.getFieldName();
                String[] fieldNameArray = StringUtils.splitByCharacterTypeCamelCase(fieldName);
                String fieldNameString = null;
                for (int i = 0; i < fieldNameArray.length; i++) {
                    if (fieldNameString != null) {
                        fieldNameString += " " + fieldNameArray[i].toLowerCase();
                    } else {
                        fieldNameString = fieldNameArray[i].toLowerCase();
                    }

                }

                String errorMessage = "Invalid " + fieldNameString.replaceAll("\\s", "").replaceAll("_gst_", " ");
                errors.put(fieldName, errorMessage);
            });

        }
        Error error = new Error(errors);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    private String getFieldName(String constraint) {
        if (org.springframework.util.StringUtils.hasText(constraint)) {
            if ("UK_contact_contactNumber".equals(constraint)) {
                return "phone number";
            }
        }
        return null;
    }

}
