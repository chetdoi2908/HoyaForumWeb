package com.example.hoya.services;

import com.example.hoya.entities.Topic;
import com.example.hoya.enums.Status;
import com.example.hoya.repositories.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopicServiceImpl implements TopicService{

    @Autowired
    TopicRepository topicRepository;

    @Override
    public Topic createTopicWithName(String inputtedTopic) {
        Topic topic = new Topic();
        topic.setName(inputtedTopic);
        topic.setIsActive(Status.ACTIVE);
        topicRepository.saveAndFlush(topic);
        return topic;
    }

    @Override
    public boolean updateTopicName(Long id, String name) {
        Topic result = topicRepository.findById(id).get();
        result.setName(name);
        topicRepository.saveAndFlush(result);
        return true;
    }


    @Override
    public List<Topic> findAllByStatus() {
        List<Topic> topic = topicRepository. findByIsActive(Status.ACTIVE);
        return topic;
    }

}
