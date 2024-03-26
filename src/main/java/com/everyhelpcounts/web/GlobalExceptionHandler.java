package com.everyhelpcounts.web;

import com.everyhelpcounts.web.models.PortalException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception ex) {
        Map<String, Object> responseBody = new HashMap<>();

        if (ex instanceof PortalException portalException) {
            setResponseValues(ex, responseBody, portalException.getCode(), portalException.getMessage(), portalException.getUserDescription());
        } else if (ex instanceof EmptyResultDataAccessException emptyResultDataAccessException) {
            setResponseValues(ex, responseBody, HttpStatus.NO_CONTENT.value(), emptyResultDataAccessException.getMessage(), "Unable to process request due to empty result set.");
        } else if (ex instanceof DataAccessException dataAccessException) {
            setResponseValues(ex, responseBody, HttpStatus.SERVICE_UNAVAILABLE.value(), dataAccessException.getMessage(), "Unable to process request due to data access error.");
        } else {
            setResponseValues(ex, responseBody, HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), "Unable to process the request due to internal error.");
        }

        return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    private void setResponseValues(Exception ex, Map<String, Object> responseBody, long code, String description, String message) {
        responseBody.put("code", code);
        responseBody.put("description", ex.getClass().getName() + " : " + description);
        responseBody.put("message", message);
    }
}
