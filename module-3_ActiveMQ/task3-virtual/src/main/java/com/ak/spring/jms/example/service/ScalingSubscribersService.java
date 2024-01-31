package com.ak.spring.jms.example.service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.jms.*;
import java.lang.IllegalStateException;
import java.util.Objects;

@Slf4j
@Service
public class ScalingSubscribersService {
    private static final String VIRTUAL_TOPIC_NAME = "VirtualTopic.Channel";
    private static final String CLIENT_ID = "consumerClientId";
    private static final String CONSUMER_PREFIX = "Consumer.";
    private static final String SUBSCRIBER_PREFIX = "subscriber-";

    private final JmsTemplate jmsTemplate;

    @Autowired
    public ScalingSubscribersService(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @SneakyThrows
    public void setSubscribersAndListen(int subscribersCount) {
        for (int i = 1; i <= subscribersCount; i++) {
            try(Connection connection = Objects.requireNonNull(jmsTemplate.getConnectionFactory()).createConnection()) {
                connection.setClientID(CLIENT_ID + i);

                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                String subscriberName = SUBSCRIBER_PREFIX + i;

                Queue queue = session.createQueue(CONSUMER_PREFIX + CLIENT_ID + i + "." + VIRTUAL_TOPIC_NAME);

                final MessageConsumer consumer1 = session.createConsumer(queue);
                final MessageConsumer consumer2 = session.createConsumer(queue);

                listenQueue(subscriberName, consumer1, "consumer-1");
                listenQueue(subscriberName, consumer2, "consumer-2");

                connection.start();
            } catch (JMSException e) {
                throw new IllegalStateException("Failed to establish connection", e);
            }
        }
        log.info("{} subscribers waiting for messages...", subscribersCount);
    }

    private static void listenQueue(String subscriberName, MessageConsumer consumer, String consumerId) throws JMSException {
        consumer.setMessageListener(message -> {
            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                try {
                    log.info("{} received message: {} with help of {}", subscriberName, textMessage.getText(), consumerId);
                } catch (JMSException e) {
                    throw new IllegalStateException("Unable to extract text from message", e);
                }
            }
        });
    }
}
