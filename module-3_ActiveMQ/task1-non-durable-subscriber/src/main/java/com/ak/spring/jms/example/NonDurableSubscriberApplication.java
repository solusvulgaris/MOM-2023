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
public class NonDurableSubscriberApplication
{
    public static void main( String[] args ) {
        SpringApplication.run(NonDurableSubscriberApplication.class, args);
    }
}
