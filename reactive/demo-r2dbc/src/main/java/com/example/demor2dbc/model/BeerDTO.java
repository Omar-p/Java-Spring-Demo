package com.example.demor2dbc.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BeerDTO {

  private Integer id;

  @NotBlank
  @Size(min = 3, max = 255)
  private String beerName;
  @Size(min = 3, max = 255)
  private String beerStyle;
  @Size(max = 25)
  private String upc;
  private Integer quantityOnHand;
  private BigDecimal price;
  private LocalDateTime createdDate;
  private LocalDateTime lastModifiedDate;

}