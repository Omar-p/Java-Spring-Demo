package com.example.demor2dbc.repositories;

import com.example.demor2dbc.domain.Beer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;

import java.math.BigDecimal;

@DataR2dbcTest
class BeerRepositoryTest {


  @Autowired
  BeerRepository beerRepository;

  @Test
  void saveBeer() {
    Beer beer = getTestBeer();
    beerRepository.save(beer)
        .subscribe(System.out::println);
  }

  Beer getTestBeer() {
    return Beer.builder()
        .beerName("BeerName")
        .beerStyle("BeerStyle")
        .upc("123456789012")
        .quantityOnHand(12)
        .price(new BigDecimal("12.99"))

        .build();
  }

}