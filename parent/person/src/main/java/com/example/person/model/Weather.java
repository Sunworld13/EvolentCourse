package com.example.person.model;

import lombok.Data;

@Data
public class Weather {
    private double temp;
    private double feels_like;
    private double temp_min;
    private double temp_max;
    private int pressure;
    private int humidity;
}