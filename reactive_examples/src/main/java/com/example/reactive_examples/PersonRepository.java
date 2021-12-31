package com.example.reactive_examples;

import com.example.reactive_examples.domain.Person;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PersonRepository {
    Mono<Person> getById(Integer id);

    Flux<Person> findAll();
}
