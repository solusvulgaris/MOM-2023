package com.ak.spring.kafka.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Taxi Application Entry Point
 *
 */
@Slf4j
@SpringBootApplication
public class TaxiApplication {
    public static void main( String[] args ) {
        SpringApplication.run(TaxiApplication.class, args);
    }
}
