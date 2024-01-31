package com.ak.spring.kafka.example.beans;

import lombok.Data;

@Data
public class DistanceInfo {
    private String taxiId;
    private double distance;
}
