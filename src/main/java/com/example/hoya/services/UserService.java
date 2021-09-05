package com.example.hoya.services;

import com.example.hoya.entities.CreateUserModel;
import com.example.hoya.entities.User;
import com.example.hoya.entities.UserPrincipal;

public interface UserService {

    UserPrincipal findByUsername(String username);

    User findByEmail(String email);

    String createUser(CreateUserModel user);

    boolean deleteUser(Long userid);

    String enableUser(String token);

    void resetPasswordUser(String email,String password);

    UserPrincipal findById(Long id);

    boolean checkLoginEmail(String email);

}
