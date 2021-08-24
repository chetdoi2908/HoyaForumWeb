package com.example.hoya.services;

import com.example.hoya.entities.Token;
import com.example.hoya.repositories.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService{

    @Autowired
    private TokenRepository tokenRepository;


    @Override
    public Token createToken(Token token) {
        return tokenRepository.saveAndFlush(token);
    }
}
