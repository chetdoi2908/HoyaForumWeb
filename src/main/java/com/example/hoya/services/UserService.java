package com.example.hoya.services;

import com.example.hoya.entities.CreateUserModel;
import com.example.hoya.entities.Token;
import com.example.hoya.entities.User;
import com.example.hoya.entities.UserPrincipal;

public interface UserService {

    UserPrincipal findByUsername(String username);

    Token createUser(CreateUserModel user);

    boolean deleteUser(Long userID);

    User resetPassword(String username, String password);

}
