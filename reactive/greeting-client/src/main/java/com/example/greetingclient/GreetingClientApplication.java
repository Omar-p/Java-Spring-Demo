package com.example.greetingclient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class GreetingClientApplication {


  public static void main(String[] args) {
    SpringApplication.run(GreetingClientApplication.class, args);
  }

  @Bean
  WebClient webClient(WebClient.Builder builder) {
    return builder
        .baseUrl("http://localhost:8080")
//        .filter(ExchangeFilterFunctions.basicAuthentication("user", "password"))
        .build();
  }
}

@Component
@Slf4j
class Client {

  private final WebClient webClient;
  private final ReactiveCircuitBreaker reactiveCircuitBreaker;

  Client(WebClient webClient, ReactiveCircuitBreakerFactory reactiveCircuitBreakerFactory) {
    this.webClient = webClient;
    this.reactiveCircuitBreaker = reactiveCircuitBreakerFactory.create("greeting");
  }

  @EventListener(ApplicationReadyEvent.class)
  public void ready() {
    /*
    // hedging
    Flux<String> host1 = null; // get all endpoints for the service from the discovery client
    Flux<String> host2 = null;
    Flux<String> host3 = null;

    Flux<String> first = Flux.first(host1, host2, host3);
*/
    var http = this.webClient
        .get()
        .uri("/greeting/{name}", "omar")
//        .cookies()
        .retrieve()

        .bodyToMono(GreetingResponse.class);
       /* .retry(3)
//        .repeatWhen()
        .onErrorMap(e -> new IllegalStateException("Error " + e.getMessage()))
        .onErrorResume(IllegalStateException.class, e -> Mono.just(new GreetingResponse("Error " + e.getMessage()))) // only work on IllegalStateException
        .subscribe(gr -> log.info("single: {}", gr.getMessage()));*/

    reactiveCircuitBreaker.run(
        http,
        throwable -> Mono.just(new GreetingResponse("Hello, world!"))
    ).subscribe(gr -> log.info("single: {}", gr.getMessage()));

    /*
    this.webClient
        .get()
        .uri("/greeting/{name}", "omar")
//        .cookies()
        .retrieve()

        .bodyToMono(GreetingResponse.class)
        .retry(3)
//        .repeatWhen()
        .onErrorMap(e -> new IllegalStateException("Error " + e.getMessage()))
        .onErrorResume(IllegalStateException.class, e -> Mono.just(new GreetingResponse("Error " + e.getMessage()))) // only work on IllegalStateException
        .subscribe(gr -> log.info("single: {}", gr.getMessage()));

     */

    /*
    this.webClient
        .get()
        .uri("/greetingmany/{name}", "omar")
//        .cookies()
        .retrieve()
        .bodyToFlux(GreetingResponse.class)
        .subscribe(gr -> log.info("SSE: {}", gr.getMessage()));

     */
  }

}

@Data
@AllArgsConstructor
@NoArgsConstructor
class GreetingResponse {
  private String message;
}

@Data @AllArgsConstructor @NoArgsConstructor
class GreetingRequest {
  private String name;
}


