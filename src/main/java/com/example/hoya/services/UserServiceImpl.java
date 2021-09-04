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
    public User findByEmail(String email) {
        User user = userRepository.findUserByEmail(email); // chỗ này nó tìm ko dc thì phải cíu t :v
        return user;
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
        String link = "http://localhost:8080/user/confirm?token=" + token;
        emailService.send(user.getEmail(), buildCreateUserEmail(user.getUsername(),link), "Tạo tài khoản");

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
    public void resetPasswordUser(String email) {
        User user = userRepository.findUserByEmail(email);
        user.setPassword(user.getPassword());
        userRepository.save(user);
    }

    @Override
    public UserPrincipal findById(Long id) {
        User user = userRepository.findById(id).get();
        UserPrincipal userPrincipal = new UserPrincipal();
        userPrincipal.setUserId(user.getId());
        userPrincipal.setUsername(user.getUsername());
        userPrincipal.setEmail(user.getEmail());
        userPrincipal.setRole(user.getRole());
        return userPrincipal;
    }

    private String buildCreateUserEmail(String name, String link) {
        return "<p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Chào " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Xin vui lòng bấm vào đường link bên dưới để kích hoạt tài khoản: </p><p><a href=\"" + link + "\">Kích hoạt</a></p><p>Cảm ơn</p>";
    }
}
