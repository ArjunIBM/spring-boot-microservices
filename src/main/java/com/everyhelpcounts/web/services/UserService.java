package com.everyhelpcounts.web.services;

import com.everyhelpcounts.web.models.PortalException;
import com.everyhelpcounts.web.models.User;

import java.util.List;

public interface UserService {

    List<User> retrieveUsers() throws PortalException;
}
