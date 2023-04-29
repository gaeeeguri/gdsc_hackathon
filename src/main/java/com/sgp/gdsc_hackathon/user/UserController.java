package com.sgp.gdsc_hackathon.user;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public Iterable<User> getUsers() {
        return userService.findUsers();
    }

    @PostMapping("/users")
    public Long createUser(@RequestBody User user) {
        return userService.join(user);
    }
}
