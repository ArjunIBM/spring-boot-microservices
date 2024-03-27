package com.everyhelpcounts.web.repository.impl;

import com.everyhelpcounts.web.models.ERole;
import com.everyhelpcounts.web.models.Role;
import com.everyhelpcounts.web.models.User;
import com.everyhelpcounts.web.repository.UserRoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Repository
public class UserRoleRepositoryImpl implements UserRoleRepository {

    private static final Logger logger = LoggerFactory.getLogger(UserRoleRepositoryImpl.class);

    private JdbcTemplate jdbcTemplate;

    UserRoleRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Set<Role> findUserRoles(String username) {
        String sql = "SELECT r.id, r.name FROM users u, user_roles ur, roles r WHERE u.id = ur.user_id AND ur.role_id = r.id AND u.username = ?";

        return new HashSet<>(jdbcTemplate.query(
                sql,
                new Object[]{username},
                (rs, rowNum) -> {
                    Role role = new Role();
                    role.setId(rs.getLong("id"));
                    role.setName(ERole.valueOf(rs.getString("name")));
                    return role;
                }
        ));
    }

    @Override
    public Role addUserRole(User user, ERole name) {
        Role role = null;
        String sql = "INSERT INTO user_roles (user_id, role_id, status_cd) VALUES (?, (select id from roles where name = ?), ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, user.getId());
            ps.setString(2, name.name());
            ps.setString(3, "A");
            return ps;
        }, keyHolder);

        if (keyHolder.getKeys() != null && !keyHolder.getKeys().isEmpty()) {
            Map<String, Object> keys = keyHolder.getKeys();
            role = new Role();
            role.setId(((Integer) keys.get("id")).longValue());
            role.setName(name);
        }
        return role;
    }


}

