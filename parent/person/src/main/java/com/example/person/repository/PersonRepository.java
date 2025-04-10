package com.example.person.repository;

import com.example.person.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {
    // Поиск по точному совпадению имени
    Optional<Person> findByFirstname(String firstname);

    boolean existsByFirstname(String firstname);
}