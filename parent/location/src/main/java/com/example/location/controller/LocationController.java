package com.example.location.controller;

import com.example.location.model.Location;
import com.example.location.model.Weather;
import com.example.location.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/location")
public class LocationController {

    private final LocationRepository repository;
    private final RestTemplate restTemplate;

    @Autowired
    public LocationController(LocationRepository repository, RestTemplate restTemplate) {
        this.repository = repository;
        this.restTemplate = restTemplate;
    }

    @GetMapping
    public ResponseEntity<?> getLocations(@RequestParam(required = false) String name) {
        if (name != null) {
            return repository.findByName(name)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        }
        return ResponseEntity.ok(repository.findAll());
    }

    @PostMapping
    public ResponseEntity<Location> addLocation(@RequestBody Location location) {
        if (repository.existsByName(location.getName())) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repository.save(location));
    }

    @PutMapping
    public ResponseEntity<Location> updateLocation(
            @RequestParam String name,
            @RequestBody Location updatedLocation) {
        return repository.findByName(name)
                .map(existing -> {
                    existing.setLatitude(updatedLocation.getLatitude());
                    existing.setLongitude(updatedLocation.getLongitude());
                    return ResponseEntity.ok(repository.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteLocation(@RequestParam String name) {
        return repository.findByName(name)
                .map(location -> {
                    repository.delete(location);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/weather")
    public ResponseEntity<Weather> getWeather(@RequestParam String name) {
        return repository.findByName(name)
                .map(location -> {
                    String url = String.format("http://localhost:8082/weather?lat=%f&lon=%f",
                            location.getLatitude(), location.getLongitude());
                    return ResponseEntity.ok(restTemplate.getForObject(url, Weather.class));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}