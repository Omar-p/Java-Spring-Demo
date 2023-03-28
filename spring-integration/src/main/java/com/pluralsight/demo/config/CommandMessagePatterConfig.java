package com.pluralsight.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.messaging.MessageChannel;

@Configuration
@EnableIntegration
public class CommandMessagePatterConfig {

  @Bean
  public MessageChannel swagChannel() {
    return new DirectChannel();
  }




}
