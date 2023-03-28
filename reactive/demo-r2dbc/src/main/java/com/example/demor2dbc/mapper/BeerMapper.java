package com.example.demor2dbc.mapper;

import com.example.demor2dbc.domain.Beer;
import com.example.demor2dbc.model.BeerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface BeerMapper {
  Beer beerDtoToBeer(BeerDTO dto);

  BeerDTO beerToBeerDto(Beer beer);
}