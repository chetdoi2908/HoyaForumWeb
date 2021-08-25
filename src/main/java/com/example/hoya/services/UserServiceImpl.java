package com.example.hoya.services;

import com.example.hoya.entities.CreateUserModel;
import com.example.hoya.entities.Role;
import com.example.hoya.entities.User;
import com.example.hoya.entities.UserPrincipal;
import com.example.hoya.enums.Status;
import com.example.hoya.repositories.UserRepository;
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
    public User createUser(CreateUserModel userModel) {
        User user = new User();
        user.setUsername(userModel.getUsername());
        user.setPassword(userModel.getPassword());
        user.setRole(roleService.findById(2L));
        user.setStatus(Status.ACTIVE);
        return userRepository.saveAndFlush(user);
    }

    @Override
    public User updatePasswordByUsername(User user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }

}
