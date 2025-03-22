package org.example.ex14.controller;

import org.example.ex14.dto.Person;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/person")
public class PersonController {

    private final AtomicInteger counter = new AtomicInteger(1);

    private List<Person> persons = new ArrayList<>(Arrays.asList(
            new Person(1, "Ivan", "Ivanovich", "Ivanov", LocalDate.of(1999, 2,3)),
            new Person(2, "Петр", "Петрович", "Петров", LocalDate.of(2002, 2,2)),
            new Person(3, "Евгений", "Васильевич", "Васин", LocalDate.of(2005, 4,8)),
            new Person(4, "Максим", "Яковлевич", "Окопский", LocalDate.of(1978, 6,5))
    ));

    @GetMapping
    public List<Person> getAllPersons() {
        return persons;
    }

    @GetMapping("/{id}")
    public Person getPersonById(@PathVariable int id) {
        return persons.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Person not found"));
    }

    @PostMapping
    public Person addPerson(@RequestBody Person person) {
        person.setId(counter.getAndIncrement());
        persons.add(person);
        return person;
    }

    @PutMapping("/{id}")
    public Person updatePerson(@PathVariable int id, @RequestBody Person updatedPerson) {
        Person person = getPersonById(id);
        person.setFirstname(updatedPerson.getFirstname());
        person.setSurname(updatedPerson.getSurname());
        person.setLastname(updatedPerson.getLastname());
        person.setBirthday(updatedPerson.getBirthday());
        return person;
    }

    @DeleteMapping("/{id}")
    public void deletePerson(@PathVariable int id) {
        persons.removeIf(p -> p.getId() == id);
    }
}