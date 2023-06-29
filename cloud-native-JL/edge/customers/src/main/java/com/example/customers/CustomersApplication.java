package com.example.customers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@SpringBootApplication
public class CustomersApplication {

  public static void main(String[] args) {
    SpringApplication.run(CustomersApplication.class, args);
  }

}

record Customer(Long id, String name) {}
@RestController
class CustomerController {


  private final List<Customer> db = List.of(
    new Customer(1L, "Jane Doe"),
    new Customer(2L, "Josh Long"),
    new Customer(3L, "Roy Clarkson"),
    new Customer(4L, "Violetta Komyshan"),
    new Customer(5L, "Trisha Gee"),
    new Customer(6L, "Mark Heckler"),
    new Customer(7L, "Olga Maciaszek-Sharma")
  );

  @GetMapping("/customers")
  Flux<Customer> get() {
    return Flux.fromIterable(this.db);
  }
}