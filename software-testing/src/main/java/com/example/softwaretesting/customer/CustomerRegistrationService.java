package com.example.softwaretesting.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerRegistrationService {

  private final CustomerRepository customerRepository;

  @Autowired
  public CustomerRegistrationService(CustomerRepository customerRepository) {
    this.customerRepository = customerRepository;
  }

  public void registerNewCustomer(CustomerRegistrationRequest request) {
    final Optional<Customer> customerByPhoneNumber = customerRepository.findCustomerByPhoneNumber(request.phoneNumber());
    customerByPhoneNumber.ifPresentOrElse(customer -> {
      if (request.name().equals(customer.getName())) {
        return;
      }
      throw new IllegalStateException("phone number taken");
    }, () -> customerRepository.save(new Customer(request.name(), request.phoneNumber())));
  }
}