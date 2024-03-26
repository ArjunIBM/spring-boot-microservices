package com.everyhelpcounts.web.repository.impl;

import com.everyhelpcounts.web.models.ERole;
import com.everyhelpcounts.web.repository.RoleRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Set;

@Repository
public class RoleRepositoryImpl implements RoleRepository {
    private JdbcTemplate jdbcTemplate;

    RoleRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean hasRole(ERole name) throws DataAccessException {
        String sql = "SELECT COUNT(*) FROM roles WHERE name = ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, name.name());
        return count > 0;
    }
}
