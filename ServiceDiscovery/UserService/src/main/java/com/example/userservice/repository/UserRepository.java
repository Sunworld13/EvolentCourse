package com.example.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

import com.example.userservice.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
}
