package com.example.person.controller;

import com.example.person.model.Person;
import com.example.person.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/person")
public class PersonController {

    private final PersonRepository repository;

    @Autowired
    public PersonController(PersonRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<?> getPersons(@RequestParam(required = false) String name) {
        if (name != null) {
            return repository.findByFirstname(name)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        }
        return ResponseEntity.ok(repository.findAll());
    }

    @PostMapping
    public ResponseEntity<Person> addPerson(@RequestBody Person person) {
        if (person.getId() != null && repository.existsById(person.getId())) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repository.save(person));
    }

    @PutMapping
    public ResponseEntity<Person> updatePerson(
            @RequestParam Long id,
            @RequestBody Person updatedPerson) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setFirstname(updatedPerson.getFirstname());
                    existing.setSurname(updatedPerson.getSurname());
                    existing.setLastname(updatedPerson.getLastname());
                    existing.setBirthday(updatedPerson.getBirthday());
                    existing.setLocation(updatedPerson.getLocation());
                    return ResponseEntity.ok(repository.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping
    public ResponseEntity<Void> deletePerson(@RequestParam Long id) {
        return repository.findById(id)
                .map(person -> {
                    repository.delete(person);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/weather")
    public ResponseEntity<?> getPersonWeather(@RequestParam Long id) {
        return repository.findById(id)
                .map(person -> {
                    
                    return ResponseEntity.ok("Weather data for " + person.getLocation());
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
