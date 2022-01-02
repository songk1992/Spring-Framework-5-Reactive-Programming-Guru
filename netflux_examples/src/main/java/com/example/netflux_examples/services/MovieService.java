package com.example.netflux_examples.services;

import com.example.netflux_examples.domain.Movie;
import com.example.netflux_examples.domain.MovieEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MovieService {
    Mono<Movie> getMovieById(String id);
    Flux<Movie> getAllMovies();
    Flux<MovieEvent> streamMovieEvents(String id);
}
