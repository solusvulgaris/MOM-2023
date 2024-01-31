package com.ak.spring.kafka.example.services;

import com.ak.spring.kafka.example.beans.DistanceInfo;
import com.ak.spring.kafka.example.beans.TaxiSignal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DistanceCalculatorService {
    @Autowired
    @Qualifier("kafkaDistanceInfoTemplate")
    private final KafkaTemplate<String, DistanceInfo> kafkaTemplate;

    private final Map<String, TaxiSignal> latestSignals = new HashMap<>();

    public DistanceCalculatorService(KafkaTemplate<String, DistanceInfo> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(
            topics = "input_topic",
            groupId = "taxi-consumer-group",
            concurrency = "3"
    )
    public void consumeSignal(@Payload TaxiSignal signal) {
        double distance = calculateDistance(signal);
        latestSignals.put(signal.getTaxiId(), signal);
        sendDistanceInfoToOutputTopic(signal.getTaxiId(), distance);
    }

    private double calculateDistance(TaxiSignal signal) {
        TaxiSignal previousSignal = latestSignals.getOrDefault(signal.getTaxiId(), null);

        if (previousSignal != null) {
            double lat1 = previousSignal.getLatitude();
            double lon1 = previousSignal.getLongitude();
            double lat2 = signal.getLatitude();
            double lon2 = signal.getLongitude();

            //using Haversine formula:
            //a = sin²(Δφ/2) + cos φ1 ⋅ cos φ2 ⋅ sin²(Δλ/2)
            //c = 2 ⋅ atan2( √a, √(1−a) )
            //d = R ⋅ c
            double dLat = Math.toRadians(lat2 - lat1);
            double dLon = Math.toRadians(lon2 - lon1);

            double a = Math.pow(Math.sin(dLat / 2), 2) +
                    Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                            Math.pow(Math.sin(dLon / 2), 2);

            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

            double radius = 6371;//Earth radius

            return radius * c;
        } else {
            return 0.0;
        }
    }

    private void sendDistanceInfoToOutputTopic(String taxiId, double distance) {
        DistanceInfo distanceInfo = new DistanceInfo();
        distanceInfo.setTaxiId(taxiId);
        distanceInfo.setDistance(distance);

        kafkaTemplate.send("output_topic", taxiId, distanceInfo);
    }
}
