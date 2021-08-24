package com.example.hoya.repositories;

import com.example.hoya.entities.User;
import com.example.hoya.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByUsernameAndStatus(String username, Status status);
}
