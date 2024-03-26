package com.everyhelpcounts.web.controllers;

import com.everyhelpcounts.web.models.PortalException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class BaseController {

    public static ResponseEntity<Object> handleResponse(Object obj) {
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    public static ResponseEntity<Object> handleException(String message, Exception ex) {
        if (ex instanceof PortalException portalException) {
            return new ResponseEntity<>(portalException, HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseBody.put("description", ex.getMessage());
            responseBody.put("message", "An error occurred");
            return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
