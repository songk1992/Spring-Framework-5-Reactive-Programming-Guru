package com.example.reactivebeerclient.client;

import com.example.reactivebeerclient.model.BeerDto;
import com.example.reactivebeerclient.model.BeerPagedList;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface BeerClient {
    Mono<BeerDto> getBeerById(UUID id, Boolean showInventoryOnHand);
    Mono<BeerPagedList> listBeers(Integer pageNumber, Integer pageSize, String beerName, String beerStyle, Boolean showInventoryOnHand);
    Mono<ResponseEntity<Void>> createBeer(BeerDto beerDto);
    Mono<ResponseEntity> updateBeer(BeerDto beerDto);
    Mono<ResponseEntity> deleteBeerById(UUID id);
    Mono<BeerDto> getBeerByUPC(String upc);
}
