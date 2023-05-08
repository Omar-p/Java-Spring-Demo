package com.example.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@SpringBootApplication
@EnableDiscoveryClient
public class ServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(ServiceApplication.class, args);
  }

}

@RestController
class GreetingController {

  private long random(int seconds) {
    return new Random().nextInt(seconds) * 1000L;
  }

  @GetMapping("/greeting")
  String greet() throws InterruptedException {
    long delay = Math.max(random(5), random(5));
    var msg = "Hello, World! After " + delay + " milliseconds";
    System.out.println(msg);
    Thread.sleep(delay);
    return msg;
  }
}
