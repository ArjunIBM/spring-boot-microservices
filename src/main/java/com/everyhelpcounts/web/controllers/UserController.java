package com.everyhelpcounts.web.controllers;

import com.everyhelpcounts.web.models.User;
import com.everyhelpcounts.web.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import static com.everyhelpcounts.web.controllers.BaseController.handleResponse;

@RestController
@RequestMapping("/users")
public class UserController {

    UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("")
    ResponseEntity<Object> retrieveUsers(){

          List<User> users =  userService.retrieveUsers();
          return handleResponse(users);

    }

}
