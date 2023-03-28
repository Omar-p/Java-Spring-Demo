package com.pluralsight.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Service;

@MessageEndpoint
@Service
public class SwagService {

  Logger LOGGER = LoggerFactory.getLogger(SwagService.class);

  @ServiceActivator(inputChannel = "swagChannel")
  public void sendSwag(String swag) {
      LOGGER.info("Swag: {}", swag);
  }
}
