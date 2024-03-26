package com.everyhelpcounts.web.repository;

import com.everyhelpcounts.web.models.ERole;
import com.everyhelpcounts.web.models.Role;
import org.springframework.dao.DataAccessException;

import java.util.Set;

public interface RoleRepository {

    boolean hasRole(ERole name) throws DataAccessException;
}
