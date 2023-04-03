package com.example.packaging;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@SpringBootApplication
public class PackagingApplication {


  @GetMapping("/")
  Map<String, String> hello() {
    return Map.of("message", "Hello from Spring Boot!");
  }
  public static void main(String[] args) {
    SpringApplication.run(PackagingApplication.class, args);
  }

}
