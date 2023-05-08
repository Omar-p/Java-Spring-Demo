package com.example.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDiscoveryClient
public class ServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(ServiceApplication.class, args);
  }

}

@RestController
class GreetingController {

  @GetMapping("/greeting")
  String greet() {
    return "Hello, World!";
  }
}
