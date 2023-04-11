package com.example.softwaretesting.payment;

import com.example.softwaretesting.customer.Customer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Payment {

  @Id
  @GeneratedValue
  private Long paymentId;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "customer_id")
  private Customer customer;

  private BigDecimal amount;

  private Currency currency;

  private String source;

  private String description;


}
