package com.example.userservice.service;

import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RestTemplate restTemplate;
    @Value("${order.url}")
    private String orderUrl;

    public ResponseEntity<User> createUser(User user) {
        if (!userRepository.existsById(user.getId())) {
            return new ResponseEntity(userRepository.save(user), HttpStatus.CREATED);
        }
        return new ResponseEntity(userRepository.findById(user.getId()), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<User> getUser(Long id) {
        if (userRepository.existsById(id)) {
            return new ResponseEntity(userRepository.findById(id), HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<HttpStatus> deleteUser(Long id) {
        if (userRepository.findById(id).isPresent()) {
            //Посылаем запрос на удаление всех заказов пользователя
            restTemplate.delete("http://"+orderUrl+"/order/user/" + id);
            userRepository.deleteById(id);
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity(userRepository.findAll(), HttpStatus.OK);
    }

    public ResponseEntity<User> updateUser(Long id, User user) {
        if (userRepository.findById(id).isPresent()) {
            user.setId(id);
            userRepository.save(user);
            return new ResponseEntity(user, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    public Boolean isUserExist(Long id) {
        return userRepository.existsById(id);
    }


}
