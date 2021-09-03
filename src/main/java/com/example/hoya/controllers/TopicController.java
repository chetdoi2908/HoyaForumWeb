package com.example.hoya.controllers;

import com.example.hoya.entities.Topic;
import com.example.hoya.services.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/topic")
public class TopicController {

    @Autowired
    TopicService topicService;

    @PostMapping("/createTopicWithName")
    public HttpStatus createTopicWithName(@RequestBody Topic topic){

        boolean result = topicService.createTopicWithName(topic);
        if(result){
            return HttpStatus.OK;
        }
        return HttpStatus.BAD_REQUEST;
    }

    @PutMapping("/updateTopicName")
    public HttpStatus updateTopicName(@RequestBody Topic topic){
        boolean result = topicService.updateTopicName(topic.getId(), topic.getName());
        if(result){
            return HttpStatus.OK;
        }
        return HttpStatus.BAD_REQUEST;
    }

}
