package com.example.hoya.services;

import com.example.hoya.entities.Token;
import com.example.hoya.entities.User;
import com.example.hoya.entities.UserPrincipal;
import com.example.hoya.repositories.TokenRepository;
import com.example.hoya.repositories.UserRepository;
import com.example.hoya.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Service
public class TokenServiceImpl implements TokenService{

    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtil jwtUtil;


    @Override
    public Token createToken(User user) {
        Token token = new Token();
        token.setToken(jwtUtil.generateToken(user));
        LocalDateTime localDateTime = LocalDateTime.ofInstant(jwtUtil.generateExpirationDate().toInstant(), ZoneId.systemDefault());
        token.setTokenExpDate(localDateTime);
        token.setCreatedBy(user.getId());
        return tokenRepository.saveAndFlush(token);
    }


    @Override
    @Transactional
    public String confirmToken(String token) {
        System.out.println("đây là token" + token);
            Token tokenModel = tokenRepository.findTokenByToken(token);
        if(tokenModel.getUseAt() != null){
            System.out.println("đùng rồi");
        }
        LocalDateTime expiredAt = tokenModel.getTokenExpDate();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token hết hạn");
        }
        tokenModel.setUseAt(LocalDateTime.now());
        tokenRepository.save(tokenModel);
        User user = userRepository.findById(tokenModel.getCreatedBy()).get();
        userService.enableUser(user.getUsername());
        return "Đã confirm thành công";
    }

}
