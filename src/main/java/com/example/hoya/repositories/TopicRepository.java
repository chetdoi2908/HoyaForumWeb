package com.example.hoya.repositories;

import com.example.hoya.entities.Topic;
import com.example.hoya.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {

    List<Topic> findByIsActive(Status isActive);
}
