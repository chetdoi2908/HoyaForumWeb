package com.example.hoya.controllers;

import com.example.hoya.entities.*;
import com.example.hoya.enums.Status;
import com.example.hoya.repositories.RoleRepository;
import com.example.hoya.services.RoleService;
import com.example.hoya.services.TokenService;
import com.example.hoya.services.UserService;
import com.example.hoya.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RoleService roleService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user){
        UserPrincipal userPrincipal = userService.findByUsername(user.getUsername());
        if(user == null || !new BCryptPasswordEncoder().matches(user.getPassword(), userPrincipal.getPassword())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("tài khoản hoặc mật khẩu không chính xác");
        }
        Token token = new Token();
        token.setToken(jwtUtil.generateToken(userPrincipal));
        token.setTokenExpDate(jwtUtil.generateExpirationDate());
        token.setCreatedBy(userPrincipal.getUserId());
        tokenService.createToken(token);
        System.out.println(jwtUtil.generateToken(userPrincipal));
        return ResponseEntity.ok(token.getToken());
    }

    @PostMapping("/register")
    public CreateUserModel register(@RequestBody CreateUserModel user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus(Status.ACTIVE);
        user.setRole("ROLE_USER");
        userService.createUser(user);
        return user;
    }

    @PostMapping("/resetpassword")
    public User resetPassword(@RequestBody User user, String password){
        userService.updatePasswordByUsername(user, password);
        return user;
    }

}
