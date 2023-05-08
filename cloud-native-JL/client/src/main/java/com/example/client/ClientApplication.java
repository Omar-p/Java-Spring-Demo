package com.example.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@SpringBootApplication
@EnableDiscoveryClient
public class ClientApplication {

  public static void main(String[] args) {
    SpringApplication.run(ClientApplication.class, args);
  }

  @Bean
  @LoadBalanced
  WebClient.Builder webClientBuilder() {
    return WebClient.builder();
  }

  @Bean
  WebClient webClient(WebClient.Builder builder) {
    return builder.build();
  }
}

@RestController
class GreetingController {

  private final WebClient webClient;

  GreetingController(WebClient webClient) {
    this.webClient = webClient;
  }

  @GetMapping("/client")
  Mono<String> greet() {
    return webClient
      .get()
      .uri("http://SERVICE/greeting")
      .retrieve()
      .bodyToMono(String.class);
  }
}
