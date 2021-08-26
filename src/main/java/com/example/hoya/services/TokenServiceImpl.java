package com.example.hoya.services;

import com.example.hoya.entities.Token;
import com.example.hoya.repositories.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TokenServiceImpl implements TokenService{

    @Autowired
    private TokenRepository tokenRepository;


    @Override
    public Token createToken(Token token) {
        return tokenRepository.saveAndFlush(token);
    }


    @Override
    public String confirmToken(String token) {
        Token tokentmp = tokenRepository.findByToken(token);
        if(tokentmp.getUseAt() != null){
            System.out.println("đùng rồi");
        }
        LocalDateTime expiredAt = tokentmp.getTokenExpDate();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token hết hạn");
        }
        tokenRepository.updateUseAt(token, LocalDateTime.now());
        return "Đã confirm thành công";
    }


}
