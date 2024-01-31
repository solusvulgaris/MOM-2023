package com.ak.spring.jms.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

/**
 * Subscriber Entry Point
 *
 */
@EnableJms
@SpringBootApplication
public class DurableSubscriberApplication
{
    public static void main( String[] args ) {
        SpringApplication.run(DurableSubscriberApplication.class, args);
    }
}
