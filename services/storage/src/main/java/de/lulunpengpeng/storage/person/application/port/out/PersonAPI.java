package de.lulunpengpeng.storage.person.application.port.out;

import de.lulunpengpeng.storage.person.domain.Person;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface PersonAPI {
    Flux<Person> getAllPerson();

    Flux<Person> getAllPerson(String name);

    Mono<Person> findPersonById(UUID id);

    Mono<Person> updatePerson(Person person);

    Mono<Person> createPerson(Person person);

    Mono<Void> deletePerson(UUID personId);

    Flux<Person> saveAll(List<Person> persons);

    Mono<Void> deleteAll();
}
