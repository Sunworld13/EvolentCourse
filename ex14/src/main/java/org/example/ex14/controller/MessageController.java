package org.example.ex14.controller;

import org.example.ex14.dto.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class MessageController {
    private final List<Message> messages = new ArrayList<>();
    private final AtomicInteger counter = new AtomicInteger(1);

    public MessageController() {
        // Инициализация тестовыми данными
        messages.add(new Message(counter.getAndIncrement(), "Первое сообщение",
                "Текст первого сообщения", LocalDateTime.now()));
        messages.add(new Message(counter.getAndIncrement(), "Второе сообщение",
                "Текст второго сообщения", LocalDateTime.now().minusHours(1)));
    }

    @GetMapping("/message")
    public ResponseEntity<List<Message>> getAllMessages() {
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }
//    @GetMapping("/message")
//    public Iterable<Person> getAllMessages() {
//        return messages;
//    }

    @GetMapping("/message/{id}")
    public ResponseEntity<Message> getMessageById(@PathVariable int id) {
        Message message = findMessageById(id);
        if (message != null) {
            return new ResponseEntity<>(message, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
    }
//    @GetMapping("/message/{id}")
//    public Optional<Message> findMessageById(@PathVariable int id) {
//        return messages.stream().filter(p -> p.getId() == id).findFirst();
//    }


    @PostMapping("/message")
    public Message addMessage(@RequestBody Message message) {
        messages.add(message);
        return message;
    }

    @PutMapping("/message/{id}")
    public ResponseEntity<Message> updateMessage(@PathVariable int id, @RequestBody Message message) {
        Message existingMessage = findMessageById(id);
        if (existingMessage != null) {
            messages.set(messages.indexOf(existingMessage),message);
            return new ResponseEntity<>(message, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(addMessage(message), HttpStatus.CREATED);
        }
    }

    @DeleteMapping("/message/{id}")
    public void deleteMessage(@PathVariable int id) {
        messages.removeIf(p->p.getId()==id);
    }

    private Message findMessageById(int id) {
        return messages.stream().filter(m -> m.getId() == id).findFirst().orElse(null);
    }
}