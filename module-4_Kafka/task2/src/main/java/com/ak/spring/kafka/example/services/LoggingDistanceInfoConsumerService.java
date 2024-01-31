package com.ak.spring.kafka.example.services;

import com.ak.spring.kafka.example.beans.DistanceInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LoggingDistanceInfoConsumerService {
    @KafkaListener(
            topics = "output_topic",
            groupId = "logger-distance-consumer-group"
    )
    public void consumeDistanceInfo(@Payload DistanceInfo distanceInfo) {
        final String distance = String.format("%.2f", distanceInfo.getDistance());
        log.info("Taxi car with id: {} covered {} km.", distanceInfo.getTaxiId(), distance);
    }
}
