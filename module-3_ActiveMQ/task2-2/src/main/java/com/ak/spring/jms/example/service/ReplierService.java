package com.ak.spring.jms.example.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;

@Slf4j
@Service
public class ReplierService {
    private static final String DESTINATION_QUEUE = "request.channel.queue";

    private final JmsTemplate jmsTemplate;

    @Autowired
    public ReplierService(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @JmsListener(destination = DESTINATION_QUEUE)
    public void receiveAndReply(String request, final Message message) throws JMSException {
        log.info("Replier received request: " + request);
        Destination replyDestination = message.getJMSReplyTo();
        String replyMessage = "Reply to: " + request;
        jmsTemplate.send(replyDestination, session -> {
            Message responseMsg = session.createTextMessage(replyMessage);
            responseMsg.setJMSReplyTo(message.getJMSReplyTo());
            responseMsg.setJMSCorrelationID(message.getJMSCorrelationID());
            return responseMsg;
        });
    }
}
