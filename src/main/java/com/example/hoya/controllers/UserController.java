package com.example.hoya.controllers;

import com.example.hoya.entities.*;
import com.example.hoya.enums.Status;
import com.example.hoya.services.OtpService;
import com.example.hoya.services.UserService;
import com.example.hoya.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private OtpService otpService;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginModel user){
        UserPrincipal userPrincipal = userService.findByUsername(user.getUsername());
        User user1 = new User();
        user1.setUsername(user.getUsername());
        user1.setPassword(user.getPassword());
        if(user == null || !new BCryptPasswordEncoder().matches(user.getPassword(), userPrincipal.getPassword())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("tài khoản hoặc mật khẩu không chính xác");
        }
        String token = jwtUtil.generateToken(user1);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/register")
    public String register(@RequestBody CreateUserModel user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        String token = userService.createUser(user);
        return token;
    }

    @GetMapping(path = "confirm")
    public String confirm(@RequestParam("token") String token) {

         return userService.enableUser(token);
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

    @PostMapping("/sendResetPasswordEmail")
    public ResponseEntity<Object> sendResetPasswordEmail(@RequestParam("email") String email) throws MessagingException {

        Map<String, String> response = new HashMap<>(2);

        // generate OTP.
        Boolean isGenerated = otpService.generateOtp(email);
        if (!isGenerated)
        {
            response.put("status", "error");
            response.put("message", "OTP can not be generated.");

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // success message
        response.put("status", "success");
        response.put("message", "OTP successfully generated. Please check your e-mail!");

//        userService.sendEmailResetPassword(email, isGenerated);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Info FE để sẵn, chỉ cho user nhập password
    @PostMapping("/resetpassword")
    public HttpStatus resetPassword(@RequestBody UserPrincipal inputtedUser)
    {
        userService.resetPasswordUser(inputtedUser);
        return HttpStatus.OK;
    }
    @PostMapping("/validate")
    public HttpStatus validateOTP(@RequestParam(name = "email") String email,@RequestParam(name = "otp") Integer otp)
    {
        // validate provided OTP. nhu nay ha
        Boolean isValid = otpService.validateOTP(email, otp);
        if (!isValid)
        {
            return HttpStatus.BAD_REQUEST;
        }

        return HttpStatus.OK;
    }



}
