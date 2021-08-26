package de.lulunpengpeng.storage.person.application;

import de.lulunpengpeng.storage.person.application.port.out.PersonAPI;
import de.lulunpengpeng.storage.person.domain.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Slf4j
@Profile("!test")
public class PersonDataInitializer {
    private PersonAPI personAPI;

    @Autowired
    public PersonDataInitializer(PersonAPI personAPI) {
        this.personAPI = personAPI;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void setupData() {
        List<Person> persons = Stream.of("eric", "phillip", "alex", "sören", "rene", "nico")
                .map(Person::newFrom)
                .collect(Collectors.toList());

        this.personAPI.deleteAll()
                .doOnSuccess( done -> log.info("all persons are removed"))
                .thenMany(this.personAPI.saveAll(persons))
                .thenMany(personAPI.getAllPerson())
                .subscribe(person -> log.info("find saved person {}", person));
    }
}
