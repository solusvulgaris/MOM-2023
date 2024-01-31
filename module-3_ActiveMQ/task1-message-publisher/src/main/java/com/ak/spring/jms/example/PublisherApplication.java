package com.ak.spring.jms.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

/**
 * Publisher Entry Point
 *
 */
@EnableJms
@SpringBootApplication
public class PublisherApplication {
    public static void main( String[] args ) {
        SpringApplication.run(PublisherApplication.class, args);
    }
}
