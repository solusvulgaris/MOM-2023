package com.ak.spring.jms.example.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

@EnableJms
@Configuration
public class ReplierConfig {
    public static final String USER_NAME = "admin";
    public static final String PASSWORD = "admin";
    public static final String BROKER_URL = "tcp://localhost:61616";

/*    @Bean
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }*/

    @Bean
    public ActiveMQConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(USER_NAME, PASSWORD, BROKER_URL);
      //  factory.setClientID("client");
        return factory;
    }

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
        DefaultJmsListenerContainerFactory listenerContainerFactory = new DefaultJmsListenerContainerFactory();
        listenerContainerFactory.setConnectionFactory(connectionFactory());
        //listenerContainerFactory.setSubscriptionDurable(true);
        //listenerContainerFactory.setConcurrency("1-1");
        //listenerContainerFactory.setMessageConverter(jacksonJmsMessageConverter());

        return listenerContainerFactory;
    }
}