package com.example.reactive_examples;

import com.example.reactive_examples.domain.Person;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

public class PersonRepositoryImpl implements PersonRepository{

    Person kimc = new Person(1, "C", "Kim");
    Person kimd = new Person(2, "d", "Kim");
    Person kime = new Person(3, "e", "Kim");
    Person kimf = new Person(4, "f", "Kim");

    @Override
    public Mono<Person> getById(Integer id) {
        return findAll().filter(person -> Objects.equals(person.getId(), id)).next();
    }

    @Override
    public Flux<Person> findAll() {
        return Flux.just(kimc, kimd, kime, kimf);
    }
}
