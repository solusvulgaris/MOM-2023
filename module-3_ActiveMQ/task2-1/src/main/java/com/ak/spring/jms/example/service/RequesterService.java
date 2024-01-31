package com.ak.spring.jms.example.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TemporaryQueue;
import javax.jms.TextMessage;

@Slf4j
@Service
public class RequesterService {
    private static final String DESTINATION_QUEUE = "request.channel.queue";

    private final JmsTemplate jmsTemplate;

    @Autowired
    public RequesterService(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public String sendAndReceive(String request) {
        try {
            Message repliedMessage = jmsTemplate.sendAndReceive(DESTINATION_QUEUE, session -> {
                TemporaryQueue replyChannelTempQueue = session.createTemporaryQueue();
                Message requestMessage = session.createTextMessage(request);
                requestMessage.setJMSReplyTo(replyChannelTempQueue);
                requestMessage.setJMSCorrelationID(request);
                return requestMessage;
            });
            if (repliedMessage instanceof TextMessage) {
                log.info("Requester received " + ((TextMessage) repliedMessage).getText());
                return ((TextMessage) repliedMessage).getText();
            }
            return "Unknown result of delivery. No reply from server.";
        } catch (JMSException e) {
            return e.getMessage();
        }

    }

}
