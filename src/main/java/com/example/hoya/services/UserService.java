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

    void resetPasswordUser(UserPrincipal user);

    UserPrincipal findById(Long id);

}
