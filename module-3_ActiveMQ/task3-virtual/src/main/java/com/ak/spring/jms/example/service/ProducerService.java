package com.ak.spring.jms.example.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProducerService {
    private static final String VIRTUAL_TOPIC_NAME = "VirtualTopic.Channel";
    private final JmsTemplate jmsTemplate;

    @Autowired
    public ProducerService(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void sendMessageToVirtualTopic(String message) {
        log.info("Producer sent message: " + message);
        jmsTemplate.convertAndSend(
                new ActiveMQTopic(VIRTUAL_TOPIC_NAME),
                message
        );
    }

}
