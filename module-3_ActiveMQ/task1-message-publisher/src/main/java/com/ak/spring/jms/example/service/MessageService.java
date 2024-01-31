package com.ak.spring.jms.example.service;

import com.ak.spring.jms.example.pojos.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MessageService {
    private static final String MESSAGE_QUEUE = "message.queue";

    private long messageCount = 0;

    public long getMessageCount() {
        return messageCount;
    }

    public void processNewMessage() {
        ++messageCount;
    }

    public void rollbackLastMessage() {
        --messageCount;
    }

    @Autowired
    private JmsTemplate jmsTemplate;

    public void send(Message message){
        jmsTemplate.setPubSubDomain(true);
        jmsTemplate.setDeliveryPersistent(true);
        jmsTemplate.convertAndSend(MESSAGE_QUEUE, message);
        log.info("Publisher send: " +  message);
    }
}
