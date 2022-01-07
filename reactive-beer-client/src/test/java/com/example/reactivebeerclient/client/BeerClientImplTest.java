package com.example.reactivebeerclient.client;

import com.example.reactivebeerclient.config.WebClientConfig;
import com.example.reactivebeerclient.model.BeerDto;
import com.example.reactivebeerclient.model.BeerPagedList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BeerClientImplTest {

    BeerClientImpl beerClient;

    @BeforeEach
    void setUp() {
        beerClient = new BeerClientImpl(new WebClientConfig().webClient());
    }

    @Test
    void listBeers() {
        Mono<BeerPagedList> beerPagedListMono = beerClient.listBeers(null, null, null, null, null);
        BeerPagedList pagedList = beerPagedListMono.block();
        assertThat(pagedList).isNotNull();
        assertThat(pagedList.getContent().size()).isGreaterThan(0);
        System.out.println(pagedList.toList());
    }

    @Test
    void listBeersPageSize10() {
        Mono<BeerPagedList> beerPagedListMono = beerClient.listBeers(1, 10, null, null, null);
        BeerPagedList pagedList = beerPagedListMono.block();
        assertThat(pagedList).isNotNull();
        assertThat(pagedList.getContent().size()).isEqualTo(10);
        System.out.println(pagedList.toList());
    }

    @Test
    void listBeersNoRecords() {
        Mono<BeerPagedList> beerPagedListMono = beerClient.listBeers(10, 20, null, null, null);
        BeerPagedList pagedList = beerPagedListMono.block();
        assertThat(pagedList).isNotNull();
        assertThat(pagedList.getContent().size()).isEqualTo(0);
        System.out.println(pagedList.toList());
    }

    @Test
    void getBeerById() {
        Mono<BeerPagedList> beerPagedListMono = beerClient.listBeers(1, 10, null, null, null);
        BeerPagedList pagedList = beerPagedListMono.block();
        UUID beerId = pagedList.toList().get(0).getId();

        Mono<BeerDto> beerDtoMono = beerClient.getBeerById(beerId, true);
        assertThat(beerDtoMono).isNotNull();
        BeerDto beerDto = beerDtoMono.block();
        System.out.println(beerDto);
        assertThat(beerDto.getId()).isEqualTo(beerId);
    }

    @Test
    void getBeerByUPC() {
        Mono<BeerPagedList> beerPagedListMono = beerClient.listBeers(1, 10, null, null, null);
        BeerPagedList pagedList = beerPagedListMono.block();
        String beerUpc = pagedList.toList().get(0).getUpc();

        Mono<BeerDto> beerDtoMono = beerClient.getBeerByUPC(beerUpc);
        BeerDto beerDto = beerDtoMono.block();
        System.out.println(beerDto);
        assertThat(beerDto.getUpc()).isEqualTo(beerUpc);
    }


    @Test
    void createBeer() {
        BeerDto beerDto = BeerDto.builder()
                .beerName("HelloBeer")
                .beerStyle("IPA")
                .upc("2345678912345")
                .price(new BigDecimal("10.99"))
                .build();
        Mono<ResponseEntity<Void>> responseEntityMono = beerClient.createBeer(beerDto);
        ResponseEntity responseEntity = responseEntityMono.block();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void updateBeer() {
        Mono<BeerPagedList> beerPagedListMono = beerClient.listBeers(null, null, null, null, null);
        BeerPagedList pagedList = beerPagedListMono.block();

        BeerDto beerDto = pagedList.getContent().get(0);
        BeerDto updatedBeer = BeerDto.builder()
                .beerName("Really Good Beer")
                .beerStyle(beerDto.getBeerStyle())
                .price(beerDto.getPrice())
                .upc(beerDto.getUpc())
                .build();

        Mono<ResponseEntity<Void>> responseEntityMono = beerClient.updateBeer(beerDto.getId(), updatedBeer);
        ResponseEntity<Void> responseEntity = responseEntityMono.block();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void testDeleteBeerHandleException() {

        Mono<ResponseEntity<Void>> responseEntityMono = beerClient.deleteBeerById(UUID.randomUUID());

        ResponseEntity<Void> responseEntity = responseEntityMono.onErrorResume(throwable -> {
            if (throwable instanceof WebClientResponseException) {
                WebClientResponseException exception = (WebClientResponseException) throwable;
                return Mono.just(ResponseEntity.status(exception.getStatusCode()).build());
            } else {
                throw new RuntimeException();
            }
        }).block();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }


    @Test
    void deleteBeerByIdNotFound() {
        Mono<ResponseEntity<Void>> responseEntityMono = beerClient.deleteBeerById(UUID.randomUUID());
        assertThrows(WebClientResponseException.class, () -> {
            ResponseEntity<Void> responseEntity = responseEntityMono.block();
            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        });
    }


    @Test
    void deleteBeerById() {
        Mono<BeerPagedList> beerPagedListMono = beerClient.listBeers(null, null, null, null, null);
        BeerPagedList pagedList = beerPagedListMono.block();
        BeerDto beerDto = pagedList.getContent().get(0);

        Mono<ResponseEntity<Void>> responseEntityMono = beerClient.deleteBeerById(beerDto.getId());
        ResponseEntity<Void> responseEntity = responseEntityMono.block();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    }

}