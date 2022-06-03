package com.example.spring_boot.controllers;

import com.example.spring_boot.models.User;
import com.example.spring_boot.services.MyUserDetailsService;
import com.example.spring_boot.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/registration")
public class RegistrationController {

    private final UserService userService;

    @Autowired
    public RegistrationController(MyUserDetailsService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void registration(@RequestBody @Validated User user) {
        userService.addUser(user);
    }
}
