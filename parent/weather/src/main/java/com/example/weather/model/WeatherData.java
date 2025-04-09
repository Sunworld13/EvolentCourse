package com.example.weather.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class WeatherData {
    @Id
    private String coordinates; // format "lat,lon"

    private String conditions;
    private Double temperature;
    private LocalDateTime lastUpdated;
}
