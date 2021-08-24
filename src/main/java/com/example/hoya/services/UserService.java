package com.example.hoya.services;

import com.example.hoya.entities.User;

public interface UserService {
    User getUserByUsername(String username);
}
