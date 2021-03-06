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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.ZoneId;

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


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user){
        UserPrincipal userPrincipal = userService.findByUsername(user.getUsername());
        if(user == null || !new BCryptPasswordEncoder().matches(user.getPassword(), userPrincipal.getPassword())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("tài khoản hoặc mật khẩu không chính xác");
        }
        Token token = new Token();
        token.setToken(jwtUtil.generateToken(user));
        LocalDateTime localDateTime = LocalDateTime.ofInstant(jwtUtil.generateExpirationDate().toInstant(), ZoneId.systemDefault());
        token.setTokenExpDate(localDateTime);
        token.setCreatedBy(userPrincipal.getUserId());
        tokenService.createToken(user);
        return ResponseEntity.ok(token.getToken());
    }

    @PostMapping("/register")
    public String register(@RequestBody CreateUserModel user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus(Status.INACTIVE);
        user.setRole("ROLE_USER");
        String token = userService.createUser(user);
        return token;
    }

    @GetMapping(path = "confirm")
    public String confirm(@RequestParam("token") String token) {

         return tokenService.confirmToken(token);
    }

    @DeleteMapping("/delete/{userid}")
    public HttpStatus delete(@PathVariable(name = "userid") Long userid){
        if(userService.deleteUser(userid))
        {
            return HttpStatus.OK;
        }else{
            return HttpStatus.BAD_REQUEST;
        }
    }

    @RequestMapping(value="/logout", method=RequestMethod.GET)
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/";
    }

    @PostMapping("/reset")
    public String sendResetPasswordEmail(@RequestParam String email){
        return userService.sendEmailResetPassword(email);
    }

    @GetMapping(path = "resetpassword")
    public User getTokenFromLink(@RequestParam("token") String token) {
        User user = tokenService.getUserFromToken(token);
        return user;
    }

    // Info FE để sẵn, chỉ cho user nhập password
    @PostMapping("/resetpassword")
    public HttpStatus resetPassword(@RequestBody UserPrincipal inputtedUser)
    {
        userService.resetPasswordUser(inputtedUser);
        return HttpStatus.OK;
    }



}
