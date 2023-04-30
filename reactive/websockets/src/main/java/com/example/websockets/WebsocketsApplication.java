package com.example.websockets;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Stream;

@SpringBootApplication
public class WebsocketsApplication {

  public static void main(String[] args) {
    SpringApplication.run(WebsocketsApplication.class, args);
  }

}

@Configuration
class GreetingWebSocketConfiguration {
  @Bean
  SimpleUrlHandlerMapping simpleUrlHandlerMapping(WebSocketHandler webSocketHandler) {
    return new SimpleUrlHandlerMapping() {{
      setUrlMap(Map.of("/ws/greetings", webSocketHandler));
      setOrder(10);
    }};
  }

  @Bean
  WebSocketHandler webSocketHandler(GreetingService greetingService) {
    return session -> {
      final Flux<WebSocketMessage> receive = session.receive();
      final Flux<String> greetingResponseFlux = receive.map(WebSocketMessage::getPayloadAsText)
          .map(GreetingRequest::new)
          .flatMap(greetingService::greetMany)
          .map(GreetingResponse::getMessage);

      return session.send(
          greetingResponseFlux.map(session::textMessage)
      );
    };
  }

  @Bean
  WebSocketHandlerAdapter webSocketHandlerAdapter() {
    return new WebSocketHandlerAdapter();
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


@Service
class GreetingService {

  private GreetingResponse greet(String name) {
    return new GreetingResponse("Hello, " + name + ", " + LocalDateTime.now().toString());
  }

  Flux<GreetingResponse> greetMany(GreetingRequest greetingRequest) {
    return Flux.fromStream(
        Stream.generate(
            () -> greet(greetingRequest.getName())
        )
    ).delayElements(java.time.Duration.ofSeconds(2));
  }
}