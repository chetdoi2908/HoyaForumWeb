package com.example.hoya.repositories;

import com.example.hoya.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    Token findTokenByToken(String token);

    //@Query("UPDATE Token SET Token.useAt = ?2 where Token.token = ?1")
    //String  updateUseAt(String token, LocalDateTime useAt);
}
