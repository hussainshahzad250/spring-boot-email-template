package com.sas.controller;

import com.sas.entity.User;
import com.sas.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/register")
public class UserController {

    @Autowired
    private UsersService usersService;

    @PostMapping
    public String addUser(@RequestBody User user) {
        return usersService.addUser(user);
    }

}
