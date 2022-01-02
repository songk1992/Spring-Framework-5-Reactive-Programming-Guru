package com.example.netflux_examples.bootstrap;

import com.example.netflux_examples.domain.Movie;
import com.example.netflux_examples.repositories.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@Component
public class initMovies implements CommandLineRunner {

    private final MovieRepository movieRepository;

    @Override
    public void run(String... args) throws Exception{
        movieRepository.deleteAll()
                .thenMany(Flux.just("Silence of the lambdas", "Meet Fluxes", "Lord of the Fluxes", "Back to the Flux")
                        .map(Movie::new)
                        .flatMap(movieRepository::save))
                .subscribe(null, null, ()-> movieRepository.findAll().subscribe(System.out::println));
    }
}
