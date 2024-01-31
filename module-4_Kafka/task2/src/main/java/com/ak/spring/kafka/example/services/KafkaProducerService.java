package com.ak.spring.kafka.example.services;

import com.ak.spring.kafka.example.beans.TaxiSignal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaProducerService {

    @Autowired
    @Qualifier("kafkaSequentialTemplate")
    private KafkaTemplate<String, TaxiSignal> kafkaSequentialTemplate;

    public boolean sendSignal(TaxiSignal signal) {
        if (isValidSignal(signal)) {
            kafkaSequentialTemplate.send("input_topic", signal.getTaxiId(), signal);
            return true;
        } else {
            log.error("Invalid signal: {}", signal.toString());
            return false;
        }
    }

    private boolean isValidSignal(TaxiSignal signal) {
        if (signal == null) {
            return false;
        }
        if (!signal.getTaxiId().startsWith("taxi-")) {
            return false;
        }
        if ((signal.getLatitude() > 90 || signal.getLatitude() < -90)) {
            return false;
        }
        return (signal.getLongitude() <= 180 && signal.getLongitude() >= -180);
    }
}
