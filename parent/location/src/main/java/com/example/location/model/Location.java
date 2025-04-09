package com.example.location.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String person;

    private String city;
    private Double latitude;
    private Double longitude;
}
