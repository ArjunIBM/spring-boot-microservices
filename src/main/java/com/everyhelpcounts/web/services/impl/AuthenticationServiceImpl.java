package com.everyhelpcounts.web.services.impl;

import com.everyhelpcounts.web.models.ERole;
import com.everyhelpcounts.web.models.PortalException;
import com.everyhelpcounts.web.models.Role;
import com.everyhelpcounts.web.models.User;
import com.everyhelpcounts.web.payload.request.SignupRequest;
import com.everyhelpcounts.web.repository.RoleRepository;
import com.everyhelpcounts.web.repository.UserRepository;
import com.everyhelpcounts.web.repository.UserRoleRepository;
import com.everyhelpcounts.web.services.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    UserRepository userRepository;

    RoleRepository roleRepository;

    UserRoleRepository userRoleRepository;

    PasswordEncoder encoder;

    AuthenticationServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder,
                              UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public User registerUser(SignupRequest request) throws PortalException {
       User user = null;
        try {
           if (Boolean.TRUE.equals(userRepository.existsByUsername(request.getUsername()))) {
               throw new PortalException(HttpStatus.BAD_REQUEST.value(),
                       "Username already exists!",  "Error: Username is already taken!");
           }

           if (Boolean.TRUE.equals(userRepository.existsByEmail(request.getEmail()))) {
               throw new PortalException(HttpStatus.BAD_REQUEST.value(),
                       "Email already exists!",  "Error: Email is already in use!");
           }

           // Create new user's account
           user = new User(request.getUsername(), request.getEmail(), encoder.encode(request.getPassword()));
           user = userRepository.registerUser(user);

           Set<String> strRoles = request.getRole();
           Set<Role> roles = new HashSet<>();

           if (strRoles == null) {
               addUserRole(user, roles, ERole.ROLE_USER);
           } else {
               final User userCopy = user;
               strRoles.forEach(role -> {
                   switch (role) {
                       case "admin": addUserRole(userCopy, roles, ERole.ROLE_ADMIN);
                           break;
                       case "mod": addUserRole(userCopy, roles, ERole.ROLE_MODERATOR);
                           break;
                       default: addUserRole(userCopy, roles, ERole.ROLE_USER);
                           break;
                   }
               });
           }
           user.setRoles(roles);
       } catch (Exception ex) {
           throw new PortalException(HttpStatus.PROCESSING.value(), "Unable to process user registration.", ex.getMessage());
       }
       return user;
    }

    private void addUserRole(User user, Set<Role> roles, ERole name) {
        try {
            if (roleRepository.hasRole(name)) {
               Role role = userRoleRepository.addUserRole(user, name);
                roles.add(role);
            }
        } catch (Exception ex) {
             throw new PortalException(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), "Error: Unable to assign user role" +  ERole.ROLE_USER);
        }
    }
}
