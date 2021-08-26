package de.lulunpengpeng.storage.person.adapter.out;

import de.lulunpengpeng.storage.person.domain.Person;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface PersonRepository extends R2dbcRepository<Person, UUID> {
    Flux<Person> getPersonByName(String name);
}
