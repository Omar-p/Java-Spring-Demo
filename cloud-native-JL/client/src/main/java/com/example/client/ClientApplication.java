package com.example.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;

@SpringBootApplication
@EnableDiscoveryClient
public class ClientApplication {

  public static void main(String[] args) {
    SpringApplication.run(ClientApplication.class, args);
  }

  @Bean
  WebClient webClient(WebClient.Builder builder) {
    return builder.build();
  }
}

@RestController
class GreetingController {

  private final WebClient webClient;
private final DiscoveryClient discoveryClient;
private final LoadBalancerClient lbc;
  GreetingController(WebClient webClient, DiscoveryClient discoveryClient, LoadBalancerClient lbc) {
    this.webClient = webClient;
    this.discoveryClient = discoveryClient;
    this.lbc = lbc;
  }

  @GetMapping("/client")
  Mono<String> greet() {

    var max = 3;
    var servicesInstance = this.discoveryClient.getInstances("SERVICE");
    Assert.isTrue(servicesInstance.size() >= max,
        "The number of services instances is less than " + max);
    var chosen = new HashSet<ServiceInstance>();
    while (chosen.size() < 3) {
      var randomIndex = Math.random() * servicesInstance.size();
      var index = (int) (Math.min(randomIndex * 1, servicesInstance.size()) - 1);
      var element = servicesInstance.get(index);
      chosen.add(element);
      System.out.printf("Adding %s:%s%n", element.getHost(), element.getPort());
      System.out.println("Chosen: " + chosen.size());
    }
    var requests = Flux.fromIterable(chosen)
      .flatMap(serviceInstance -> {
        var host = serviceInstance.getHost();
        var port = serviceInstance.getPort();
        var url = String.format("http://%s:%s/greeting", host, port);
        return this.webClient.get().uri(url).retrieve().bodyToMono(String.class);
      });

    return  Flux.firstWithSignal(requests)
        .take(1)
        .singleOrEmpty()
        .doOnNext(System.out::println);

  }
}
