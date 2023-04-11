package com.example.softwaretesting.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {

  @Query(
      value = """
      select id, name, phone_number from customer
      where phone_number = ?1""", nativeQuery = true
  )
  Optional<Customer> findCustomerByPhoneNumber(String phoneNumber);
}
