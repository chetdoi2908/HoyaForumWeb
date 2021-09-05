package com.example.hoya.controllers;

import com.example.hoya.entities.Topic;
import com.example.hoya.entities.UpdateTopicModel;
import com.example.hoya.services.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/topic")
public class TopicController {

    @Autowired
    TopicService topicService;

    @PostMapping("/createTopicWithName")
    public HttpStatus createTopicWithName(@RequestBody String topic){

        Topic result = topicService.createTopicWithName(topic);
        if(result!=null){
            return HttpStatus.OK;
        }
        return HttpStatus.BAD_REQUEST;
    }

    @PutMapping("/updateTopicName")
    public HttpStatus updateTopicName(@RequestBody UpdateTopicModel topic){
        boolean result = topicService.updateTopicName(topic.getId(), topic.getName());
        if(result){
            return HttpStatus.OK;
        }
        return HttpStatus.BAD_REQUEST;
    }

    @GetMapping(value = "/getAllTopic")
    public @ResponseBody List<Topic> getAllTopic() {
        List<Topic> topic = topicService.findAllByStatus();
        return topic;
    }

}
