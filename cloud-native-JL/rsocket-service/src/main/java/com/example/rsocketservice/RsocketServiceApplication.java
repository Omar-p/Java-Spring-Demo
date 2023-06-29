package com.example.rsocketservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@SpringBootApplication
public class RsocketServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(RsocketServiceApplication.class, args);
  }

}
record GreetingResponse(String message) {}

@Controller
class GreetingRsocketController {

  @MessageMapping("hello.{name}")
  GreetingResponse greet(@DestinationVariable String name) {
    return new GreetingResponse("Hello, " + name + "!");
  }
}