package com.example.hoya.controllers;

import com.example.hoya.entities.*;
import com.example.hoya.services.OtpService;
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
        user1.setId(userPrincipal.getUserId());
        user1.setUsername(user.getUsername());
        user1.setPassword(user.getPassword());
        user1.setRole(userPrincipal.getRole());
        if(user == null || !new BCryptPasswordEncoder().matches(user.getPassword(), userPrincipal.getPassword())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("tài khoản hoặc mật khẩu không chính xác");
        }
        String token = jwtUtil.generateToken(user1);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/login/success")
    public ResponseEntity<?> loginSuccess(@RequestBody String token){
        Long userid = jwtUtil.getUserIdFromJWT(token);
        UserPrincipal user = userService.findById(userid);
        String userRole = user.getRole().getName();
        return ResponseEntity.ok(userRole);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody CreateUserModel user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        String token = userService.createUser(user);
        return ResponseEntity.ok(token);
    }

    @GetMapping(path = "confirm")
    public ResponseEntity<?> confirm(@RequestParam("token") String token) {

         return ResponseEntity.ok(userService.enableUser(token));
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
            response.put("message", "Không thể khởi tạo OTP.");

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // success message
        response.put("status", "success");
        response.put("message", "Đã gửi mã otp, xin vui lòng kiểm tra email.");

//        userService.sendEmailResetPassword(email, isGenerated);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/resetpassword")
    public ResponseEntity<Object> resetPassword(@RequestBody UserResetPasswordModel userResetPasswordModel)
    {
               userService.resetPasswordUser(userResetPasswordModel.getEmail(), passwordEncoder.encode(userResetPasswordModel.getPassword()));
               return new ResponseEntity<>("Đổi mật khẩu thành công", HttpStatus.OK);
    }
    @PostMapping("/validate")
    public ResponseEntity<Object> validateOTP(@RequestBody UserResetPasswordModel userResetPasswordModel)
    {
        // validate provided OTP.
        Boolean isValid = otpService.validateOTP(userResetPasswordModel.getEmail(), userResetPasswordModel.getOtp());
        if (!isValid)
        {
            return new ResponseEntity<>("OTP không chính xác", HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(userResetPasswordModel.getEmail());
    }

    @RequestMapping(value = "/checkLoginEmail", method = RequestMethod.POST)
    public ResponseEntity<Object> checkLoginEmail(@RequestParam(name = "email") String email)
    {
        boolean checkResult = userService.checkLoginEmail(email);
        if(checkResult){
            return ResponseEntity.ok(email);
        }
        return new ResponseEntity<>("Không tìm thấy email", HttpStatus.BAD_REQUEST);
    }

}
