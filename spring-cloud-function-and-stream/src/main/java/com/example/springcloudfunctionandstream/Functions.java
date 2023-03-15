package com.example.springcloudfunctionandstream;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.util.function.Function;

@Configuration
@Slf4j
public class Functions {
  @Bean
  Function<String, String> uppercase() {
    return msg ->{
      log.info("Uppercasing {}", msg);
      return msg.toUpperCase();
    };
  }


  @Bean
  Function<String, String> reverse() {
    return msg -> {
      log.info("Reversing {}", msg);
      return new StringBuilder(msg).reverse().toString();
    };
  }

  @Bean
  Function<Flux<String>, Flux<String>> reverseReactive() {
    return flux -> flux
        .map(msg -> new StringBuilder(msg).reverse().toString());
  }

  @Bean
  Function<Message, String> reverseWithMessage() {
    return msg -> new StringBuilder(msg.getContent()).reverse().toString();
  }
}

@Data @AllArgsConstructor
 @NoArgsConstructor
class Message {

  private String content;

}