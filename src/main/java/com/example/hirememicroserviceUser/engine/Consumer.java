package com.example.hirememicroserviceUser.engine;

import com.example.hirememicroserviceUser.model.User;
import com.example.hirememicroserviceUser.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class Consumer {

    private final Logger logger = LoggerFactory.getLogger(Consumer.class);

    @Autowired
    private UserService userService;

    @KafkaListener(topics = "users", groupId = "group_id")
    public void consume(User user) throws IOException {
        logger.info(String.format("#### -> Consumed user -> %s", user));

        //save message to database
        User newUser = new User(user.getEmail(),user.getIsRecruiter());

        this.userService.save(newUser);

        logger.info(String.format("#### -> New user created -> %s", newUser.getId()));

    }
}
