package com.example.greetingservice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.stream.Stream;

@SpringBootApplication
public class GreetingServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(GreetingServiceApplication.class, args);
  }

  @Bean
  RouterFunction<ServerResponse> routerFunction(GreetingService greetingService) {
    return RouterFunctions.route()
        .GET("/greeting/{name}", request -> {
          String name = request.pathVariable("name");
          return ServerResponse.ok().body(greetingService.greetOnce(new GreetingRequest(name)), GreetingResponse.class);
        })
        .GET("/greetingmany/{name}", request -> {
          String name = request.pathVariable("name");
          return ServerResponse.ok().contentType(MediaType.TEXT_EVENT_STREAM).body(greetingService.greetMany(new GreetingRequest(name)), GreetingResponse.class);
        })
        .build();
  }
}



@Service
class GreetingService {

  private GreetingResponse greet(String name) {
    return new GreetingResponse("Hello, " + name + ", " + LocalDateTime.now());
  }

  Mono<GreetingResponse> greetOnce(GreetingRequest greetingRequest) {
    return Mono.just(greet(greetingRequest.getName()));
  }

  Flux<GreetingResponse> greetMany(GreetingRequest greetingRequest) {
    return Flux.fromStream(
        Stream.generate(
            () -> greet(greetingRequest.getName())
        )
    ).delayElements(java.time.Duration.ofSeconds(2));
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


