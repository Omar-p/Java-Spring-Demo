package com.example.rsocketgreetingservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.rsocket.ConnectionSetupPayload;
import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.SocketAcceptor;
import io.rsocket.core.RSocketServer;
import io.rsocket.transport.netty.server.TcpServerTransport;
import io.rsocket.util.DefaultPayload;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.stream.Stream;

@SpringBootApplication
public class RsocketGreetingServiceApplication {

  @SneakyThrows
  public static void main(String[] args) {
    SpringApplication.run(RsocketGreetingServiceApplication.class, args);
    System.in.read();
  }

}

@RequiredArgsConstructor
@Component
class JsonHelper {
  private final ObjectMapper objectMapper;

  @SneakyThrows
  <T> T read(String json, Class<T> type) {
    return objectMapper.readValue(json, type);
  }

  @SneakyThrows
  String write(Object o) {
    return objectMapper.writeValueAsString(o);
  }
}
@Component
@RequiredArgsConstructor
@Slf4j
class Producer {
  private final JsonHelper jsonHelper;
  private final GreetingService greetingService;

  @EventListener(ApplicationReadyEvent.class)
  public void start() {
    log.info("starting producer");

    SocketAcceptor socketAcceptor = new SocketAcceptor() {
      @Override
      public Mono<RSocket> accept(ConnectionSetupPayload connectionSetupPayload, RSocket rSocket) {
        RSocket rSocketHandler = new RSocket() {
          @Override
          public Flux<Payload> requestStream(Payload payload) {
            String json = payload.getDataUtf8();
            GreetingRequest gr = jsonHelper.read(json, GreetingRequest.class);

            return greetingService.greetMany(gr)
                .map(jsonHelper::write)
                .map(DefaultPayload::create);
          }
        };
        return Mono.just(rSocketHandler);
      }
    };

    TcpServerTransport tcpServerTransport = TcpServerTransport.create(7000);
    RSocketServer.create(socketAcceptor)
        .bind(tcpServerTransport)
        .block();
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