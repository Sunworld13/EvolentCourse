package com.example.location.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Weather {
    @Id
    private String coordinates; // format "lat,lon"

    private String conditions;
    private Double temperature;
    private LocalDateTime lastUpdated;
}
