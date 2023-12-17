package com.ak.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jms.core.JmsTemplate;

@SpringBootApplication
public class SpringbootApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootApplication.class, args);
		ConfigurableApplicationContext context = SpringApplication.run(SpringbootApplication.class, args);
		JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);

	}

}
