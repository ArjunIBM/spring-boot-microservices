package com.everyhelpcounts.web.controllers;

import com.everyhelpcounts.web.models.User;
import com.everyhelpcounts.web.payload.request.LoginRequest;
import com.everyhelpcounts.web.payload.request.SignupRequest;
import com.everyhelpcounts.web.payload.response.JwtResponse;
import com.everyhelpcounts.web.security.jwt.JwtUtils;
import com.everyhelpcounts.web.services.AuthenticationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.everyhelpcounts.web.controllers.BaseController.handleException;
import static com.everyhelpcounts.web.controllers.BaseController.handleResponse;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    AuthenticationManager authenticationManager;

    JwtUtils jwtUtils;

    AuthenticationService authenticationService;

    AuthenticationController(AuthenticationManager authenticationManager,
                             JwtUtils jwtUtils, AuthenticationService authenticationService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.authenticationService = authenticationService;

    }

    @PostMapping("/signup")
    ResponseEntity<Object> registration(@RequestBody SignupRequest request){
        try {
          User user =  authenticationService.registerUser(request);
          return handleResponse(user);
        } catch(Exception ex) {
            return handleException("Error: Unable to process user registration", ex);
        }
    }

    @PostMapping("/signin")
    @SecurityRequirement(name = "basicAuth")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        AuthenticationService.UserDetailsImpl userDetails = (AuthenticationService.UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }


}
