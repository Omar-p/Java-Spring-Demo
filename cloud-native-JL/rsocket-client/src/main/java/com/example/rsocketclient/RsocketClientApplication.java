package com.example.rsocketclient;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.rsocket.RSocketRequester;

import java.io.IOException;

@SpringBootApplication
public class RsocketClientApplication {

  public static void main(String[] args) throws IOException {
    SpringApplication.run(RsocketClientApplication.class, args);
    System.in.read();
  }

}

record GreetingResponse(String message) {}

@Configuration
class RsocketConfiguration {
  @Bean
  RSocketRequester rSocketRequester(RSocketRequester.Builder builder) {
    return builder.tcp("localhost", 9191);
  }

  @Bean
  ApplicationRunner app(RSocketRequester requester) {
    return args -> requester.route("hello.{name}", "Omar")
        .retrieveMono(GreetingResponse.class)
        .subscribe(System.out::println);
  }
}