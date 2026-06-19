package com.example.geocodingservice.dto;

import lombok.Data;

import java.util.Set;

@Data
public class TripRawNotification {
    private Double beginLon;
    private Double beginLat;
    private Double endLon;
    private Double endLat;
    private String enterprise;
    private String regNum;
    private Set<String> managers;
}
