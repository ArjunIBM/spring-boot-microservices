package com.everyhelpcounts.web.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        // Set response status to Forbidden
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        // Set content type to JSON
        response.setContentType("application/json");

        // Create a JSON error message
        String errorMessage = "{\"error\": \"Access Denied\", \"message\": \"" + authException.getMessage() + "\"}";

        // Write the JSON error message to the response
        PrintWriter writer = response.getWriter();
        writer.write(errorMessage);
        writer.flush();
    }
}
