package com.example.hoya.services;

import com.example.hoya.entities.Token;
import com.example.hoya.entities.User;
import com.example.hoya.entities.UserPrincipal;


public interface TokenService {

    Token createToken(User user);

    String confirmToken(String token);


}
