package de.lulunpengpeng.storage.person.application;

import de.lulunpengpeng.storage.person.application.port.in.PersonService;
import de.lulunpengpeng.storage.person.application.port.out.PersonAPI;
import de.lulunpengpeng.storage.person.domain.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;

@Service
public class PersonDefaultService implements PersonService {
    private PersonAPI personAPI;

    @Autowired
    public PersonDefaultService(PersonAPI personAPI) {
        this.personAPI = personAPI;
    }

    @Override
    public Flux<Person> getAllPerson(String name) {
        return Optional.ofNullable(name)
                .map(n -> personAPI.getAllPerson(name))
                .orElse(personAPI.getAllPerson());
    }

    @Override
    public Mono<Person> findPersonById(String id) {
        return personAPI.findPersonById(UUID.fromString(id));
    }

    @Override
    public Mono<Person> updatePerson(Person person) {
        return personAPI.updatePerson(person);
    }

    @Override
    public Mono<Person> createPerson(Person person) {
        return personAPI.createPerson(person);
    }

    @Override
    public Mono<Boolean> deletePerson(String personId) {
        return personAPI.deletePerson(UUID.fromString(personId)).thenReturn(true);
    }
}
