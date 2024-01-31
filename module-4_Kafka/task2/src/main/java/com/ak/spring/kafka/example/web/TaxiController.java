package com.ak.spring.kafka.example.web;

import com.ak.spring.kafka.example.beans.TaxiSignal;
import com.ak.spring.kafka.example.services.KafkaProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("api")
public class TaxiController {
    private final KafkaProducerService kafkaProducerService;

    @Autowired
    public TaxiController(KafkaProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }

    @PostMapping(
            value = "/send-signal"
    )
    public ResponseEntity<String> sendSignal(@RequestBody TaxiSignal signal) {
        final boolean result = kafkaProducerService.sendSignal(signal);

        if (Boolean.TRUE.equals(result)) {
            return ResponseEntity.ok(signal.toString() + " sent to input_topic");
        }

        log.error("Invalid signal: {}", signal.toString());
        return ResponseEntity.badRequest().body("Invalid signal.");
    }
}
