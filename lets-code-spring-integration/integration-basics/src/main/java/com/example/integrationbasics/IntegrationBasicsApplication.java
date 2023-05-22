package com.example.integrationbasics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.GenericSelector;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.messaging.support.MessageBuilder;

import java.time.Instant;

@SpringBootApplication
public class IntegrationBasicsApplication {

  public static void main(String[] args) {
    SpringApplication.run(IntegrationBasicsApplication.class, args);
  }

  @Bean
  DirectChannel aToB() {
    return new DirectChannel();
  }

  String text() {
    return Math.random() > 0.5
        ? "Hello from @" + Instant.now() + "!." :
        "hola desde @" + Instant.now() + "!.";
  }

  @Bean
  IntegrationFlow integrationFlow() {
    return IntegrationFlow.from((MessageSource<String>) () -> MessageBuilder.withPayload(text()).build(),
            poller -> poller.poller(pm -> pm.fixedDelay(1000))
        )
        .channel(aToB())
        .get();
  }

  @Bean
  IntegrationFlow flow1() {
    return IntegrationFlow.from(aToB())
        .filter(String.class, new GenericSelector<String>() {
          @Override
          public boolean accept(String source) {
            return source.contains("hola");
          }
        })
        .transform(String.class, String::toUpperCase)
        .handle(String.class, (payload, headers) -> {
          System.out.println("payload = " + payload);
          return null;
        })
        .get();

  }
}
