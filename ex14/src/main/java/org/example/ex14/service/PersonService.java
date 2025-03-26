package org.example.ex14.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.ex14.dto.Message;
import org.example.ex14.dto.Person;
import org.example.ex14.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PersonService {
    @Autowired
    private PersonRepository repository;
    //    private MessageRepository messageRepository;
//
    public Iterable<Person> getAllPersons() {
        return repository.findAll();
    }

    public Optional<Person> findPersonById(int id) {
        return repository.findById(id);
    }

    public Person addPerson(Person person) {
        return repository.save(person);
    }

    public Person updatePerson(int id, Person person) {
        Person existingPerson = repository.findById(id).orElse(null);
        if (existingPerson != null) {
            existingPerson.setFirstname(person.getFirstname());
            existingPerson.setSurname(person.getSurname());
            existingPerson.setLastname(person.getLastname());
            existingPerson.setBirthday(person.getBirthday());
            return repository.save(existingPerson);
        }
        return null;
    }
    public void deletePerson(int id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Person not found with id: " + id);
        }
        repository.deleteById(id);
    }



    public Person addMeesageToPerson(int personId, Message message) {
        Optional<Person> optionalPerson = repository.findById(personId);
        if (!optionalPerson.isPresent()) {
            throw new EntityNotFoundException("Person not found with id: " + personId);
        }
        Person person = optionalPerson.get();
        message.setPerson(person);
        message.setTime(LocalDateTime.now());
        //messageRepository.save(message);
        person.addMessage(message);
        return repository.save(person);
    }

    public void deleteMessageForPerson(int personId, int messageId) {
        Optional<Person> optionalPerson = repository.findById(personId);
        if (!optionalPerson.isPresent()) {
            throw new EntityNotFoundException("Person not found with id: " + personId);
        }
//        Optional<Message> optionalMessage = messageRepository.findById(messageId);
//        if (!optionalMessage.isPresent()) {
//            throw new EntityNotFoundException("Message not found with id: " + messageId);
//        }
        Person person = optionalPerson.get();
        Message messageToDelete = null;
        for (Message message : person.getMessages()) {
            if (message.getId() == messageId) {
                messageToDelete = message;
                break;
            }
        }
        if (messageToDelete == null) {
            throw new EntityNotFoundException("Message not found with id: " + messageId + " for person with id: " + personId);
        }

//        Message message=optionalMessage.get();

        person.removeMessage(messageToDelete);
//        messageRepository.delete(message);
        repository.save(person);
    }


    public List<Message> getMessagesForPerson(int personId) {
        Optional<Person> optionalPerson = repository.findById(personId);
        if (!optionalPerson.isPresent()) {
            throw new EntityNotFoundException("Person not found with id: " + personId);
        }
        Person person = optionalPerson.get();
        return person.getMessages();
        //return messageRepository.findAll();
    }


    public Optional<Message> getMessageForPerson(int personId, int messageId) {
        Optional<Person> optionalPerson = repository.findById(personId);
        if (!optionalPerson.isPresent()) {
            throw new EntityNotFoundException("Person not found with id: " + personId);
        }
        Person person = optionalPerson.get();
        Message messageById = null;
        for (Message message : person.getMessages()) {
            if (message.getId() == messageId) {
                messageById = message;
                break;
            }
        }
        if (messageById == null) {
            throw new EntityNotFoundException("Message not found with id: " + messageId + " for person with id: " + personId);
        }

        return Optional.of(messageById);
//      return messageRepository.findById(messageId);
    }
}