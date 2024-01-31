package com.ak.spring.kafka.example.beans;

import lombok.Data;

@Data
public class TaxiSignal {
    private String taxiId;
    private double latitude;
    private double longitude;
}
