package com.everyhelpcounts.web.repository;

import com.everyhelpcounts.web.models.PortalException;
import com.everyhelpcounts.web.models.User;
import com.everyhelpcounts.web.payload.request.SignupRequest;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface UserRepository {
    User registerUser(User user);

    User findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    List<User> retrieveUsers();
}
