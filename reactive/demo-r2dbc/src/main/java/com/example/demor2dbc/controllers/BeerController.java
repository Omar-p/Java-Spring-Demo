package com.example.demor2dbc.controllers;

import com.example.demor2dbc.model.BeerDTO;
import com.example.demor2dbc.service.BeerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping("/api/v2/beers")
@RequiredArgsConstructor
public class BeerController {

  private final BeerService beerService;


  @GetMapping
  public Flux<BeerDTO> getBeers() {
    return beerService.listBeers();
  }

  @PostMapping
  public Mono<ResponseEntity<Void>> createBeer(@Validated @RequestBody BeerDTO beerDTO) {
    return beerService.saveNewBeer(beerDTO)
        .map(s -> ResponseEntity.created(URI.create("/api/v2/beers/" + s.getId())).build());
  }

  @GetMapping("/{beerId}")
  public Mono<BeerDTO> getBeerById(@PathVariable("beerId") Integer beerId) {
    return beerService.getBeerById(beerId)
        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
  }


  @PatchMapping("/{beerId}")
  public Mono<ResponseEntity<Void>> patchBeer(@PathVariable Integer beerId,
                                              @Validated @RequestBody BeerDTO beerDTO) {
    return beerService.patchBeer(beerId, beerDTO)
        .map(s -> ResponseEntity.ok().build());
  }

  @PutMapping("/{beerId}")
  public Mono<ResponseEntity<Void>> updateBeer(@PathVariable Integer beerId,
                                               @Validated @RequestBody BeerDTO beerDTO) {
    return beerService.updateBeer(beerId, beerDTO)
        .map(s -> ResponseEntity.ok().build());
  }

  @DeleteMapping("/{beerId}")
  public Mono<ResponseEntity<Void>> deleteBeer(@PathVariable Integer beerId) {
    return beerService.deleteBeer(beerId)
        .thenReturn(ResponseEntity.noContent().build());
  }

}
