package com.example.actuator;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;

@SpringBootApplication
public class ActuatorApplication {

  public static void main(String[] args) {
    SpringApplication.run(ActuatorApplication.class, args);
  }

  @Bean
  CommandLineRunner commandLineRunner(Environment environment) {
    return args -> {
      System.out.println("USER: " + environment.getProperty("name"));
    };
  }
}

class CnjPropertySourceEnvironmentPostProcessor implements EnvironmentPostProcessor {

  @Override
  public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
    environment.getPropertySources().addFirst(new CnjPropertySource("cnj"));
  }
}

class  CnjPropertySource  extends PropertySource<String> {


  File config = new File(new File(System.getProperty("user.home")), "config.properties");
  private final AtomicReference<Properties> properties = new AtomicReference<>();
  public CnjPropertySource(String name) {
    super(name);
    Assert.state(this.config.exists(), "Config file must exist " + this.config.getAbsolutePath());
    initialize();
  }

  private synchronized void initialize() {
    try (var in = new BufferedInputStream(new FileInputStream(this.config))) {
      var prop = new Properties();
      prop.load(in);
      this.properties.set(prop);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Object getProperty(String name) {
    return properties.get().getProperty(name);
  }
}
@RestController
class MicrometerController {
  final Counter counter;
  MicrometerController(MeterRegistry meterRegistry) {
    counter = meterRegistry.counter("cnj.counter");
  }
  @GetMapping("/increment")
  public Double increment() {
    this.counter.increment();
    return counter.count();
  }
}