package com.example.demor2dbc.repositories;

import com.example.demor2dbc.domain.Beer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface BeerRepository
    extends ReactiveCrudRepository<Beer, Integer> {
}
