package org.example.ex14.controller;

import org.example.ex14.dto.Message;
import org.example.ex14.repository.MessageRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/message")
public class MessageController {
    private final MessageRepository repository;

    public MessageController(MessageRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public Iterable<Message> getAllMessages() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Message getMessageById(@PathVariable int id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found"));
    }

    @PostMapping
    public Message addMessage(@RequestBody Message message) {
        return repository.save(message);
    }

    @PutMapping("/{id}")
    public Message updateMessage(@PathVariable int id, @RequestBody Message updatedMessage) {
        updatedMessage.setId(id); // Устанавливаем тот же ID
        return repository.save(updatedMessage);
    }

    @DeleteMapping("/{id}")
    public void deleteMessage(@PathVariable int id) {
        repository.deleteById(id);
    }
}