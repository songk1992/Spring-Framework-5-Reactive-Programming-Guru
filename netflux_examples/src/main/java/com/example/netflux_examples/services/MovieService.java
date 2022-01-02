package com.example.netflux_examples.services;

import com.example.netflux_examples.domain.Movie;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MovieService {
    Mono<Movie> getMovieById(String id);
    Flux<Movie> getAllMovies();
}
