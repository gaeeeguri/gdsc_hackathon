package com.sgp.gdsc_hackathon.user;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

// TODO: add error handling

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    @Operation(summary = "get users", description = "Returns all users")
    public Iterable<User> getUsers() {
        return userService.findUsers();
    }

    @PostMapping("/users")
    @Operation(summary = "create user", description = "Create a new user and returns id")
    public Long createUser(@RequestBody User user) {
        return userService.join(user);
    }
}
