package com.example.hoya.services;

import com.example.hoya.entities.CreateUserModel;
import com.example.hoya.entities.Token;
import com.example.hoya.entities.User;
import com.example.hoya.entities.UserPrincipal;

public interface UserService {

    UserPrincipal findByUsername(String username);

    String createUser(CreateUserModel user);

    boolean deleteUser(Long userid);

    User resetPassword(String username, String password);

    User enableUser(String username);

    String sendEmailResetPassword(String email);

    void resetPasswordUser(UserPrincipal user);

}
