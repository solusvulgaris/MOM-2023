package com.ak.spring.jms.example.service;

import com.ak.spring.jms.example.pojos.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

/**
 * Receiver is also known as a message-driven POJO
 *
 */
@Service
@Slf4j
public class DurableMessageReceiver {
    private static final String TOPIC = "message.queue";

    @JmsListener(
            destination = TOPIC,
            id = "durable-sub",
            subscription = "durableSubscription",
            containerFactory = "jmsListenerContainerFactory"
    )
    public void receiveMessage(Message message) {
        log.info("Durable subscriber received message: " +  message);
    }
}