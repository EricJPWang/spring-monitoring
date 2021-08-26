package de.lulunpengpeng.storage.person.adapter.in;

import de.lulunpengpeng.storage.person.application.port.in.PersonService;
import de.lulunpengpeng.storage.person.domain.Person;
import io.micrometer.core.annotation.Timed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/person")
public class PersonController {

    private PersonService personService;
    private PersonDtoMapper personDtoMapper;

    @Autowired
    public PersonController(PersonService personService,
                            PersonDtoMapper personDtoMapper) {
        this.personService = personService;
        this.personDtoMapper = personDtoMapper;
    }

    @GetMapping("all")
    @Timed(value = "GET_ALL_PERSON", percentiles = {0.5, 0.95})
    Flux<PersonDto> getAllPersonby(@RequestParam(required = false) String name) {
        return personService.getAllPerson(name).map(personDtoMapper::mapFrom);
    }

    @GetMapping("/{id}")
    @Timed(value = "FIND_PERSON", percentiles = {0.5, 0.95})
    Mono<PersonDto> findPersonById(@PathVariable String id) {
        return personService.findPersonById(id).map(personDtoMapper::mapFrom);
    }

    @PutMapping("update")
    @Timed(value = "UPDATE_PERSON", percentiles = {0.5, 0.95})
    Mono<Person> updatePerson(@RequestBody PersonDto person) {
        return personService.updatePerson(personDtoMapper.toPerson(person));
    }

    @PostMapping("new")
    @Timed(value = "NEW_PERSON", percentiles = {0.5, 0.95})
    Mono<Person> createPerson(@RequestBody PersonDto person) {
        return personService.createPerson(personDtoMapper.toPerson(person));
    }

    @DeleteMapping("delete/{personId}")
    @Timed(value = "DELETE_PERSON", percentiles = {0.5, 0.95})
    Mono<Boolean> deletePerson(@PathVariable String personId) {
        return personService.deletePerson(personId);
    }
}
