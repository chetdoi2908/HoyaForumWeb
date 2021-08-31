package com.example.hoya.services;

import com.example.hoya.entities.Topic;

public interface TopicService{
    boolean createTopicWithName(Topic topic);

    boolean updateTopicName(Long id, String name);
}
