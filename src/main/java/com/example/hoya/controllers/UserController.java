package com.example.hoya.controllers;

import com.example.hoya.entities.User;
import com.example.hoya.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping(name = "/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("")
    public ResponseEntity<User> login(Principal principal){
        User user = userService.getUserByUsername(principal.getName());
        return  ResponseEntity.ok(user);
    }

}
