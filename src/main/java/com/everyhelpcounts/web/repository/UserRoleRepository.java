package com.everyhelpcounts.web.repository;

import com.everyhelpcounts.web.models.ERole;
import com.everyhelpcounts.web.models.Role;
import com.everyhelpcounts.web.models.User;
import com.everyhelpcounts.web.payload.request.SignupRequest;
import org.springframework.dao.DataAccessException;

import java.util.Set;

public interface UserRoleRepository {

    Set<Role> findUserRoles(String userName) throws DataAccessException;

    Role addUserRole(User user, ERole name) throws DataAccessException;
}
