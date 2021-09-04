package com.example.hoya.services;

import com.example.hoya.entities.Topic;

import java.util.List;


public interface TopicService{
    Topic createTopicWithName(String topic);

    boolean updateTopicName(Long id, String name);

    List<Topic> findAllByStatus();
}
