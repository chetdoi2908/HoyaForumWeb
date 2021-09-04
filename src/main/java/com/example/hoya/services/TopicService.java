package com.example.hoya.services;

import com.example.hoya.entities.Topic;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TopicService extends CrudRepository<Topic, Long> {
    boolean createTopicWithName(Topic topic);

    boolean updateTopicName(Long id, String name);

    List<Topic> findByIsActive();
}
