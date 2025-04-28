package com.example.location.controller;

import com.example.location.model.Location;
import com.example.location.model.Weather;
import com.example.location.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/location")
public class LocationController {

    @Autowired
    private LocationRepository repository;
    @Autowired
    private RestTemplate restTemplate;
    @Value("${weather.url}")
    String weatherUrl;


    @GetMapping("/weather")
    public ResponseEntity<?> redirectRequestWeather(@RequestParam String name) {

        Optional<Location> locationOptional = repository.findByName(name);
        if (locationOptional.isPresent()) {
            Location location = locationOptional.get();
            String url = String.format("http://%s/weather?lat=%s&lon=%s", weatherUrl, location.getLatitude(), location.getLongitude());
            return ResponseEntity.ok().body(restTemplate.getForObject(url, Weather.class));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("no such location");
    }



    @GetMapping
    public List<Location> getAllLocation() {
//        return repository.findAll();
        List<Location> locations = new ArrayList<>();
        repository.findAll().forEach(locations::add);
        return locations;
    }



    @GetMapping("/name")
    public Optional<Location> getLocationByName(@RequestParam String name) {
        return repository.findByName(name);
    }



    @PostMapping
    public ResponseEntity<Location> save(@RequestBody Location location) {
        Optional<Location> locationOptional = repository.findByName(location.getName());
        if (locationOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(locationOptional.get());
        }
        return ResponseEntity.status(201).body(repository.save(location));
    }


    @PutMapping()
    public ResponseEntity<Location> updateLocation(@RequestParam String name, @RequestBody Location location) {
        Optional<Location> locationOptional = repository.findByName(name);
        if (locationOptional.isPresent()) {
            Location locationToUpdate = locationOptional.get();
            locationToUpdate.setLatitude(location.getLatitude());
            locationToUpdate.setLongitude(location.getLongitude());
            return ResponseEntity.ok(repository.save(locationToUpdate));
        }
        return ResponseEntity.notFound().build();
    }


    @DeleteMapping()
    public ResponseEntity<Location> deleteLocation(@RequestParam String name) {

        Optional<Location> locationOptional = repository.findByName(name);
        if (locationOptional.isPresent()) {
            repository.delete(locationOptional.get());
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

}