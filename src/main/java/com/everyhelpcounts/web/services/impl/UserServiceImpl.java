package com.everyhelpcounts.web.services.impl;


import com.everyhelpcounts.web.models.PortalException;
import com.everyhelpcounts.web.models.Role;
import com.everyhelpcounts.web.models.User;
import com.everyhelpcounts.web.repository.UserRepository;
import com.everyhelpcounts.web.repository.UserRoleRepository;
import com.everyhelpcounts.web.services.AuthenticationService;
import com.everyhelpcounts.web.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserDetailsService, UserService {

  private UserRepository userRepository;

  private UserRoleRepository userRoleRepository;

  UserServiceImpl(UserRepository userRepository, UserRoleRepository userRoleRepository) {
    this.userRepository = userRepository;
    this.userRoleRepository = userRoleRepository;
  }

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = getUser(username);
    populateUserRoles(username, user);
    return AuthenticationService.UserDetailsImpl.build(user);
  }

  @Async
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void populateUserRoles(String username, User user) throws PortalException {
    try {
      Set<Role> roles = userRoleRepository.findUserRoles(username);
      if (roles == null || roles.isEmpty()) {
        throw new PortalException(HttpStatus.NOT_FOUND.value(), "User role Not Found for username: " + username, "Error: Unable to load user roles!");
      }
      user.setRoles(roles);
    } catch (PortalException pe) {
      throw pe;
    } catch (Exception e) {
      throw new PortalException(HttpStatus.FORBIDDEN.value(), e.getMessage(), "Error: Unable to load user roles!");
    }
  }

  @Async
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public User getUser(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username);
    if (user == null) {
      throw new UsernameNotFoundException("User Not Found with username: " + username);
    }
    return user;
  }

  @Override
  public List<User> retrieveUsers()  {
    List<User> users = userRepository.retrieveUsers();
    if (users == null || users.isEmpty()) {
      throw new PortalException(HttpStatus.NO_CONTENT.value(), "Unable to find users.", "Error: Users Not Found.");
    }
    return users;
  }

}
