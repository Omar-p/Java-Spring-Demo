package com.example.softwaretesting.customer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(value = {"id"}, allowGetters = true)
public class Customer {

  @Id
  @GeneratedValue(generator = "UUID")
  private UUID id;

  @NotBlank
  @Column(nullable = false)
  private String name;

  @NotBlank
  @Column(nullable = false, unique = true)
  private String phoneNumber;

  public Customer(String name, String phoneNumber) {
    this.name = name;
    this.phoneNumber = phoneNumber;
  }
}
