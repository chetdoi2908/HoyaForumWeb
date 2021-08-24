package com.example.hoya.services;

import com.example.hoya.entities.User;
import com.example.hoya.enums.Status;
import com.example.hoya.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findUserByUsernameAndStatus(username, Status.ACTIVE);
    }
}
