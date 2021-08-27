package com.example.hoya.services;

import com.example.hoya.entities.*;
import com.example.hoya.enums.Status;
import com.example.hoya.repositories.TokenRepository;
import com.example.hoya.repositories.UserRepository;
import com.example.hoya.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    TokenRepository tokenRepository;
    @Autowired
    RoleService roleService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private EmailServiceImpl emailService;


    @Override
    public UserPrincipal findByUsername(String username) {
        User user = userRepository.findUserByUsernameAndStatus(username, Status.ACTIVE);
        UserPrincipal userPrincipal = new UserPrincipal();
        if(user != null){
            List<String> authorities = new ArrayList<>();
            if(user.getRole() != null){
                user.getRole().getPermissions().forEach(p -> authorities.add(p.getPermissionKey()));
            }
            userPrincipal.setUserId(user.getId());
            userPrincipal.setUsername(user.getUsername());
            userPrincipal.setPassword(user.getPassword());
            userPrincipal.setAuthorities(authorities);
        }
        return userPrincipal;
    }

    @Override
    public String createUser(CreateUserModel userModel) {
        User user = new User();
        user.setUsername(userModel.getUsername());
        user.setPassword(userModel.getPassword());
        user.setEmail(userModel.getEmail());
        user.setRole(roleService.findById(2L));
        user.setStatus(Status.INACTIVE);
        userRepository.saveAndFlush(user);
        Token token = tokenService.createToken(user);
        String link = "http://localhost:8080/confirm?token=" + token.getToken();
        emailService.send(user.getEmail(), buildCreateUserEmail(user.getUsername(),link));

        return token.getToken();
    }
    @Override
    public boolean deleteUser(Long userID) {
        User user = userRepository.findById(userID).get();
        if(user != null){
            user.setStatus(Status.INACTIVE);
            userRepository.save(user);
            return true;
        }else{
            return false;
        }
    }

    @Override
    public User resetPassword(String token, String password) {
        Token token1 = tokenRepository.findTokenByToken(token);
        User user = userRepository.findById(token1.getCreatedBy()).get();
        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    @Override
    public User enableUser(String username) {
        User user = userRepository.findUserByUsername(username);
        user.setStatus(Status.ACTIVE);
        return userRepository.save(user);
    }

    @Override
    public String sendEmailResetPassword(String email) {
        User user = userRepository.findUserByEmail(email);
        Token token = tokenService.createToken(user);
        String link = "http://localhost:8080/reset?token=" + token.getToken();
        emailService.send(user.getEmail(), buildResetPasswordEmail(user.getUsername(),link));

        return link;
    }

    private String buildCreateUserEmail(String name, String link) {
        return "<p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Chào " + name + " nhá,</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Kích hoạt toài khoản đê: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Kích hoạt</a> </p></blockquote>\n<p>Tạm piệt</p>";
    }
    private String buildResetPasswordEmail(String name, String link) {
        return "<p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Chào " + name + " nhá,</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Tạo lại mật khẩu đê: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Tạo lại mật khẩu</a> </p></blockquote>\n<p>Tạm piệt</p>";
    }
}
