package com.example.softwaretesting.customer;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/customer-registration")
public class CustomerRegistrationController {

  private final CustomerRegistrationService customerRegistrationService;

  public CustomerRegistrationController(CustomerRegistrationService customerRegistrationService) {
    this.customerRegistrationService = customerRegistrationService;
  }

  @PutMapping
  public void registerCustomer(@Valid @RequestBody CustomerRegistrationRequest request) {
    customerRegistrationService.registerNewCustomer(request);
  }
}
