package com.example.demor2dbc.service;

import com.example.demor2dbc.mapper.BeerMapper;
import com.example.demor2dbc.model.BeerDTO;
import com.example.demor2dbc.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BeerService {

  private final BeerRepository beerRepository;
  private final BeerMapper beerMapper;

  public Flux<BeerDTO> listBeers() {
    return beerRepository.findAll()
        .map(beerMapper::beerToBeerDto);
  }

  public Mono<BeerDTO> getBeerById(Integer beerId) {
    return beerRepository
        .findById(beerId)
        .map(beerMapper::beerToBeerDto);
  }

  public Mono<BeerDTO> saveNewBeer(BeerDTO beerDTO) {
    return beerRepository.save(beerMapper.beerDtoToBeer(beerDTO))
        .map(beerMapper::beerToBeerDto);
  }

  public Mono<BeerDTO> patchBeer(Integer beerId, BeerDTO beerDTO) {
    return beerRepository.findById(beerId)
        .map(beer -> {
          beer.setBeerName(Optional.ofNullable(beerDTO.getBeerName()).orElse(beer.getBeerName()));
          beer.setBeerStyle(Optional.ofNullable(beerDTO.getBeerStyle()).orElse(beer.getBeerStyle()));
          beer.setPrice(Optional.ofNullable(beerDTO.getPrice()).orElse(beer.getPrice()));
          beer.setQuantityOnHand(Optional.ofNullable(beerDTO.getQuantityOnHand()).orElse(beer.getQuantityOnHand()));
          beer.setUpc(Optional.ofNullable(beerDTO.getUpc()).orElse(beer.getUpc()));
          return beer;
        })
        .flatMap(beerRepository::save)
        .map(beerMapper::beerToBeerDto);
  }

  public Mono<BeerDTO> updateBeer(Integer beerId, BeerDTO beerDTO) {
    return beerRepository.findById(beerId)
        .map(beer -> {
          beer.setBeerName(beerDTO.getBeerName());
          beer.setBeerStyle(beerDTO.getBeerStyle());
          beer.setPrice(beerDTO.getPrice());
          beer.setQuantityOnHand(beerDTO.getQuantityOnHand());
          beer.setUpc(beerDTO.getUpc());
          return beer;
        })
        .flatMap(beerRepository::save)
        .map(beerMapper::beerToBeerDto);
  }

  public Mono<Void> deleteBeer(Integer beerId) {
    return beerRepository
        .findById(beerId)
        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
        .flatMap(beerRepository::delete);
  }
}

