package com.example.hoya.services;

import com.example.hoya.entities.*;
import com.example.hoya.enums.Status;
import com.example.hoya.repositories.UserRepository;
import com.example.hoya.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleService roleService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;
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
        String token = jwtUtil.generateToken(user);
        String link = "http://localhost:8080/confirm?token=" + token;
        emailService.send(user.getEmail(), buildCreateUserEmail(user.getUsername(),link));

        return token;
    }
    @Override
    public boolean deleteUser(Long userid) {
        User user = userRepository.findById(userid).get();
        if(user != null){
            user.setStatus(Status.INACTIVE);
            userRepository.save(user);
            return true;
        }else{
            return false;
        }
    }

    @Override
    public String enableUser(String token) {
        User user = userRepository.findById(jwtUtil.getUserIdFromJWT(token)).get();
        user.setStatus(Status.ACTIVE);
        userRepository.save(user);
        return "Confirm thành công";
    }

    @Override
    public void resetPasswordUser(UserPrincipal userPrincipal) {
        User user = userRepository.getById(userPrincipal.getUserId());
        user.setPassword(userPrincipal.getPassword());
        userRepository.save(user);
    }

    @Override
    public UserPrincipal findById(Long id) {
        User user = userRepository.findById(id).get();
        UserPrincipal userPrincipal = new UserPrincipal();
        userPrincipal.setUserId(user.getId());
        userPrincipal.setUsername(user.getUsername());
        userPrincipal.setEmail(user.getEmail());
        return userPrincipal;
    }

    private String buildCreateUserEmail(String name, String link) {
        return "<p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Chào " + name + " nhá,</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Kích hoạt toài khoản đê: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Kích hoạt</a> </p></blockquote>\n<p>Tạm piệt</p>";
    }
}
