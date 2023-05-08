package com.example.nativespring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.annotation.Id;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@SpringBootApplication
@EnableConfigurationProperties(CnjProps.class)
public class NativeSpringApplication {

  public static void main(String[] args) {
    SpringApplication.run(NativeSpringApplication.class, args);
  }

}

record Customer(@Id Long id, String name) {}

interface CustomerRepository extends ReactiveCrudRepository<Customer, Long> {


}

@RestController
class CustomerRestController {

  private final CustomerRepository customerRepository;
  private final CnjProps cnjProps;

  CustomerRestController(CustomerRepository customerRepository, CnjProps cnjProps) {
    this.customerRepository = customerRepository;
    this.cnjProps = cnjProps;
  }

  @GetMapping("/customers")
  public Flux<Customer> getCustomers() {
    return customerRepository.findAll();
  }

  @GetMapping("/message")
  public String hello() {
    return cnjProps.getMessage();
  }
}

@RefreshScope
@ConfigurationProperties(prefix = "app")
class CnjProps {
  String message;

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}