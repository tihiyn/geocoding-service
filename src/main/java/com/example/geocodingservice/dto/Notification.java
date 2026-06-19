package com.example.geocodingservice.dto;

import lombok.Data;

import java.util.Set;

@Data
public class Notification {
    private String start;
    private String finish;
    private String enterprise;
    private String regNum;
    private Set<String> managers;
}
