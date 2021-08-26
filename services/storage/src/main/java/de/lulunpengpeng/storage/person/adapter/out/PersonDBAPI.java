package de.lulunpengpeng.storage.person.adapter.out;

import de.lulunpengpeng.storage.person.application.port.out.PersonAPI;
import de.lulunpengpeng.storage.person.domain.Person;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Component
public class PersonDBAPI implements PersonAPI {
    private PersonRepository personRepository;
    private Timer metricsTimer;

    @Autowired
    public PersonDBAPI(PersonRepository personRepository,
                       MeterRegistry meterRegistry) {
        this.personRepository = personRepository;
        this.metricsTimer = meterRegistry.timer("PERSON_REPOSITORY");
    }

    @Override
    public Flux<Person> getAllPerson() {
        return metricsTimer.record(() -> personRepository.findAll());
    }

    @Override
    public Flux<Person> getAllPerson(String name) {
        return metricsTimer.record(() -> personRepository.getPersonByName(name));
    }

    @Override
    public Mono<Person> findPersonById(UUID id) {
        return metricsTimer.record(() -> personRepository.findById(id));
    }

    @Override
    public Mono<Person> updatePerson(@NotNull Person person) {
        return metricsTimer.record(() -> personRepository.save(person));
    }

    @Override
    public Mono<Person> createPerson(@NotNull Person person) {
        return metricsTimer.record(() -> personRepository.save(person));
    }

    @Override
    public Mono<Void> deletePerson(UUID personId) {
        return metricsTimer.record(() -> personRepository.deleteById(personId));
    }

    @Override
    public Flux<Person> saveAll(List<Person> persons) {
        return metricsTimer.record(() -> personRepository.saveAll(persons));
    }

    @Override
    public Mono<Void> deleteAll() {
        return personRepository.deleteAll();
    }
}
