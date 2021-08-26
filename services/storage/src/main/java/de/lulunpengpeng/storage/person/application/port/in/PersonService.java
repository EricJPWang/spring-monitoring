package de.lulunpengpeng.storage.person.application.port.in;

import de.lulunpengpeng.storage.person.domain.Person;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PersonService {
    Flux<Person> getAllPerson(String name);

    Mono<Person> findPersonById(String id);

    Mono<Person> updatePerson(Person person);

    Mono<Person> createPerson(Person person);

    Mono<Boolean> deletePerson(String personId);
}
