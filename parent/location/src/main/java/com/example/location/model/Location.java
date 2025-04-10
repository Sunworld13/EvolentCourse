package com.example.location.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Location {
    @Id
    @GeneratedValue
    private Long id;
    private Double longitude;
    private Double latitude;
    private String name;
}