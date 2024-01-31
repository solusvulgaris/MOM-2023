package com.ak.spring.jms.example;

import com.ak.spring.jms.example.service.ProducerService;
import com.ak.spring.jms.example.service.ScalingSubscribersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Virtual Topic Entry Point
 *
 */
@SpringBootApplication
public class VirtualTopicApplication implements CommandLineRunner {
    private final ProducerService producerService;
    private final ScalingSubscribersService scalingSubscribersService;

    @Autowired
    public VirtualTopicApplication(ProducerService producerService, ScalingSubscribersService scalingSubscribersService) {
        this.producerService = producerService;
        this.scalingSubscribersService = scalingSubscribersService;
    }

    public static void main( String[] args ) {
        SpringApplication.run(VirtualTopicApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        scalingSubscribersService.setSubscribersAndListen(2);
        Thread.sleep(100);

        for (int i = 1; i <= 4; i++) {
            producerService.sendMessageToVirtualTopic("Hello world to Virtual Topic - " + i);
            Thread.sleep(2000);
        }
    }
}
