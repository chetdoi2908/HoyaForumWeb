package com.example.hoya.services;

import com.example.hoya.entities.Topic;
import com.example.hoya.enums.Status;
import com.example.hoya.repositories.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TopicSeriveImpl implements TopicService{

    @Autowired
    TopicRepository topicRepository;

    @Override
    public boolean createTopicWithName(Topic inputtedTopic) {
        Topic topic = new Topic();
        topic.setName(inputtedTopic.getName());
        topic.setIsActive(Status.ACTIVE);
        topicRepository.saveAndFlush(topic);
        return true;
    }

    @Override
    public boolean updateTopicName(Long id, String name) {
        Topic result = topicRepository.findById(id).get();
        result.setName(name);
        topicRepository.saveAndFlush(result);
        return true;
    }
}
