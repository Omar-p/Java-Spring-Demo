package com.pluralsight.springaop.example1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingAspect {
  private final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

  public void logBefore() {
    logger.info("Before method execution");
  }

  public void logAfter() {
    logger.info("After method execution");
  }
}
