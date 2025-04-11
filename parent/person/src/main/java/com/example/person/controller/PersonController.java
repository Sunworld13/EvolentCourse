package com.example.person.controller;

import com.example.person.model.Person;
import com.example.person.model.Weather;
import com.example.person.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/person")
public class PersonController {

    private final PersonRepository repository;
    private final RestTemplate restTemplate;

    @Autowired
    public PersonController(PersonRepository repository, RestTemplate restTemplate) {
        this.repository = repository;
        this.restTemplate = restTemplate;
    }

    @GetMapping
    public List<Person> getAllPersons() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> getPersonById(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Person> createPerson(@RequestBody Person person) {
        Person saved = repository.save(person);
        return ResponseEntity.status(201).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Person> updatePerson(
            @PathVariable Long id,
            @RequestBody Person personDetails) {
        return repository.findById(id)
                .map(person -> {
                    person.setFirstname(personDetails.getFirstname());
                    person.setSurname(personDetails.getSurname());
                    person.setLastname(personDetails.getLastname());
                    person.setBirthday(personDetails.getBirthday());
                    person.setLocation(personDetails.getLocation());
                    return ResponseEntity.ok(repository.save(person));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable Long id) {
        return repository.findById(id)
                .map(person -> {
                    repository.delete(person);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/weather")
    public ResponseEntity<Weather> getWeatherForPerson(@PathVariable Long id) {
        return repository.findById(id)
                .map(person -> {
                    String url = "http://location-service/location/weather?name=" + person.getLocation();
                    return ResponseEntity.ok(restTemplate.getForObject(url, Weather.class));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}