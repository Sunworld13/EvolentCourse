package org.example.ex14.controller;


import org.example.ex14.dto.Message;
import org.example.ex14.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class MessageController {
    @Autowired
    private MessageRepository repository;

    @GetMapping("/message")
    public Iterable<Message> getMessages() {
        return repository.findAll();
    }
    @GetMapping("/message/{id}")
    public Optional<Message> findMessageById(@PathVariable int id) {
        return repository.findById(id);
    }
    @PostMapping("/message")
    public Message addMessage(@RequestBody Message Message) {
        repository.save(Message);
        return Message;
    }
    @PutMapping("/message/{id}")
    public ResponseEntity<Message> updateMessage(@PathVariable int id, @RequestBody Message Message) {
        if (!repository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Message existingMessage = repository.findById(id).orElse(null);
        if (existingMessage != null) {
            existingMessage.setTitle(Message.getTitle());
            existingMessage.setText(Message.getText());
            existingMessage.setTime(Message.getTime());
            return new ResponseEntity<>(repository.save(existingMessage), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @DeleteMapping("/message/{id}")
    public void deleteMessage(@PathVariable int id) {
        repository.deleteById(id);
    }

}