package com.everyhelpcounts.web.repository.impl;

import com.everyhelpcounts.web.models.ERole;
import com.everyhelpcounts.web.models.Role;
import com.everyhelpcounts.web.models.User;
import com.everyhelpcounts.web.repository.UserRepository;
import com.everyhelpcounts.web.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private static final Logger logger = LoggerFactory.getLogger(UserRepositoryImpl.class);

    private JdbcTemplate jdbcTemplate;

    UserRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User registerUser(User user) {

        String sql = "INSERT INTO users (username, email, password, status_cd) VALUES (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, "A");
            return ps;
        }, keyHolder);

        if ((keyHolder.getKeys() != null) && !keyHolder.getKeys().isEmpty()) {
            Map<String, Object> keys = keyHolder.getKeys();
            user.setId((Long) keys.get("id"));
            user.setStatus((String) keys.get("status_cd"));
            user.setPassword((String) keys.get("password"));
            user.setRegistrationDate(CommonUtil.convertFromSQLDateToJAVADate((java.sql.Timestamp) keys.get("registration_date")));
        }

        return user;
    }

    @Override
    public User findByUsername(String username) {
        String sql = "select * from users where username = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{username},
                (rs, rowNum) -> {
                        User user = new User();
                        user.setId(rs.getLong("id"));
                        user.setEmail(rs.getString("email"));
                        user.setUsername(rs.getString("username"));
                        user.setPassword(rs.getString("password"));
                        return user;
            }
        );
    }

    @Override
    public List<User> retrieveUsers() {
        String sql = "SELECT u.id, u.username, u.email,  u.password, u.status_cd, u.registration_date, " +
                " STRING_AGG(r.name::character varying, ',' ORDER BY r.name) AS roles, u.password, u.status_cd "  +
                "FROM  "  +
                "users u, "  +
                "user_roles ur,  "  +
                "roles r "  +
                "where  "  +
                "u.id = ur.user_id "  +
                "and ur.role_id = r.id "  +
                "GROUP BY u.id, u.username, u.email,  u.password, u.status_cd, u.registration_date";
        return jdbcTemplate.query(sql,
                (rs, rowNum) -> {
                    User user = new User();
                    user.setId(rs.getLong("id"));
                    user.setEmail(rs.getString("email"));
                    user.setUsername(rs.getString("username"));
                    user.setStatus(rs.getString("status_cd"));
                    user.setRegistrationDate(rs.getDate("registration_date"));

                    Set<Role> roles = new HashSet<>();
                    String[] roleArray = rs.getString("roles").split(",");
                    for (String roleName : roleArray) {
                        Role role = new Role();
                        role.setName(ERole.valueOf(roleName.trim()));
                        roles.add(role);
                    }
                    user.setRoles(roles);

                    return user;
                }
        );

    }

    @Override
    public boolean existsByUsername(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, username);
        return count > 0;
    }

    @Override
    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count > 0;
    }

}

