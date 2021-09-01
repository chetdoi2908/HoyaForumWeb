package com.example.hoya.services;

import com.example.hoya.entities.CreateUserModel;
import com.example.hoya.entities.User;
import com.example.hoya.entities.UserPrincipal;

public interface UserService {

    UserPrincipal findByUsername(String username);

    String createUser(CreateUserModel user);

    boolean deleteUser(Long userid);

    User resetPassword(String username, String password);

    String enableUser(String token);

    String sendEmailResetPassword(String email);

    void resetPasswordUser(UserPrincipal user);

    UserPrincipal findById(Long id);

}
