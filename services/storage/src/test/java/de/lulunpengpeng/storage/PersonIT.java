package de.lulunpengpeng.storage;

import ch.qos.logback.classic.Logger;
import de.lulunpengpeng.storage.person.adapter.in.PersonDto;
import io.r2dbc.spi.ConnectionFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;

import java.util.UUID;

@ExtendWith(SpringExtension.class)
//@WebFluxTest
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
class PersonIT {

    @Autowired
    private WebTestClient webTestClient;

    @Qualifier("connectionFactory")
    @Autowired
    private ConnectionFactory connectionFactory;

    private PersonDto person_1;
    private PersonDto person_2;

    @BeforeEach
    void setUp() {
        person_1 = PersonDto.builder().id(UUID.randomUUID().toString()).name("test_1").build();
        person_2 = PersonDto.builder().id(UUID.randomUUID().toString()).name("test_2").build();

        Flux.from(connectionFactory.create())
                .flatMap(connection ->
                        connection.createBatch()
                                .add("drop table if exists person")
                                .add("create table person(`id` UUID NOT NULL DEFAULT random_uuid() PRIMARY KEY,`name` VARCHAR(255) NOT NULL)")
                                .add(String.format("insert into person(id,name) values ( '%s', '%s' ) ", person_1.getId(), person_1.getName()))
                                .add(String.format("insert into person(id,name) values ( '%s', 'test_2' ) ", person_2.getId(), person_2.getName()))
                                .execute()
                )
                .log()
                .blockLast();
    }

    @Test
    void getAllPersonby() {
        webTestClient.get()
                .uri("/api/person/all")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(PersonDto.class)
                .hasSize(2);
    }

    @Test
    void findPersonById() {
        webTestClient.get()
                .uri("/api/person/{id}", person_1.getId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(PersonDto.class)
                .isEqualTo(person_1);
    }

    @Test
    void updatePerson() {
        PersonDto person3 = person_1.toBuilder().name("test_3").build();

        webTestClient.put()
                .uri("/api/person/update")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(person3))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(PersonDto.class)
                .isEqualTo(person3);
    }

    @Test
    void createPerson() {
        PersonDto person3 = PersonDto.builder().name("test_3").build();

        webTestClient.post()
                .uri("/api/person/new")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(person3))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(PersonDto.class)
                .value(person -> Assertions.assertThat(person.getName()).isEqualTo(person3.getName()));
    }

    @Test
    void deletePerson() {
        webTestClient.delete()
                .uri("/api/person/delete/{personId}", person_1.getId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Boolean.class)
                .isEqualTo(true);
    }
}
