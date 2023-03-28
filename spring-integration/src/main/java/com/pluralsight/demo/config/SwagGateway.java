package com.pluralsight.demo.config;

import com.pluralsight.demo.model.Swag;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.Message;

@MessagingGateway(name = "swagGateway", defaultRequestChannel = "swagChannel", defaultReplyChannel = "swagChannel")
public interface SwagGateway {

  @Gateway
  void sendSwag(Message<Swag> swag);
}
