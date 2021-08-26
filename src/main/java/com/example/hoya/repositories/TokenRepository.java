package com.example.hoya.repositories;

import com.example.hoya.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Token findByToken(String token);

    @Query("UPDATE Token SET Token.useAt = ?2 where Token.token = ?1")
    int updateUseAt(String token, LocalDateTime useAt);
}
