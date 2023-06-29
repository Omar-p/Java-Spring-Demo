package com.example.graphql;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

@SpringBootApplication
public class GraphqlApplication {

  public static void main(String[] args) {
    SpringApplication.run(GraphqlApplication.class, args);
  }

}

@Controller
class CrmGraphqlController {

  private final CrmClient crmClient;

  CrmGraphqlController(CrmClient crmClient) {
    this.crmClient = crmClient;
  }

  // @SchemaMapping(typeName = "Query", field = "customers")
//  @QueryMapping("customers")
  @QueryMapping
  Flux<Customer> customers() {
    return this.crmClient.getCustomers();
  }

  @QueryMapping
  Flux <Customer> customersByName(@Argument String name) {
    return this.crmClient.getCustomersByName(name);
  }

  @SubscriptionMapping
  Flux<CustomerEvent> customerEvents(@Argument Integer customerId) {
    return this.crmClient.getCustomerEvents(customerId);
  }

  @MutationMapping
  Mono<Customer> addCustomer(@Argument String name) {
    return this.crmClient.addCustomer(name);
  }

  // separate resolver for field on Customer type
  @SchemaMapping(typeName = "Customer", field = "orders")
  Flux<Order> orders(Customer customer) {
    return this.crmClient.getOrdersForCustomerId(customer.id());
  }

}

//@Component
//class CrmRuntimeWiringConfigurer implements RuntimeWiringConfigurer {
//
//  private final CrmClient crmClient;
//
//  CrmRuntimeWiringConfigurer(CrmClient crmClient) {
//    this.crmClient = crmClient;
//  }
//
//  @Override
//  public void configure(RuntimeWiring.Builder builder) {
//    builder.type("Query", typeWiring -> typeWiring
//        .dataFetcher("customers", env -> crmClient.getCustomers())
//        .dataFetcher("customer", env -> crmClient.getCustomerById(env.getArgument("id")))
//        .dataFetcher("customerByName", env -> crmClient.getCustomersByName(env.getArgument("name")))
//        .dataFetcher("orders", env -> crmClient.getOrdersForCustomerId(env.getArgument("customerId")))
//    );
//    builder.type("Customer", typeWiring -> typeWiring
//        .dataFetcher("orders", env -> crmClient.getOrdersForCustomerId(env.getArgument("id")))
//        .dataFetcher("events", env -> crmClient.getCustomerEvents(env.getArgument("id")))
//    );
//  }
//}

@Component
class CrmClient {

  private final Map<Customer, Collection<Order>> db = new ConcurrentHashMap<>();
  private final AtomicInteger id = new AtomicInteger(1);

  CrmClient() {
    Flux.fromIterable(List.of(
        "John", "Jane", "Max", "Josh", "Peter", "Paul", "Mary", "Maria", "Mark", "Mike"
    ))
    .flatMap(this::addCustomer)
        .subscribe(customer -> {
            var list = this.db.get(customer);
            for (var i = 1; i<= ((Math.random()*100)); i++) {
              list.add(new Order(id.incrementAndGet(), customer.id()));
            }
        });
  }
  Flux<Order> getOrdersForCustomerId(Integer customerId) {
    return getCustomerById(customerId)
        .map(db::get)
        .flatMapMany(Flux::fromIterable);
  }
  Flux<Customer> getCustomers() {
    return Flux.fromIterable(db.keySet());
  }

  Flux<Customer> getCustomersByName(String name) {
    return getCustomers()
        .filter(c -> c.name().equals(name));
  }

  Mono<Customer> addCustomer(String name) {
    Customer customer = new Customer(id.incrementAndGet(), name);
    db.put(customer, new CopyOnWriteArrayList<>());
    return Mono.just(customer);
  }

  Mono<Customer> getCustomerById(Integer id) {
    return getCustomers()
        .filter(c -> c.id().equals(id)).singleOrEmpty();
  }

  Flux<CustomerEvent> getCustomerEvents(Integer id) {
    return getCustomerById(id)
        .flatMapMany( c ->
          Flux.fromStream(Stream.generate(() -> new CustomerEvent(c, Math.random() > .5 ? CustomerEventType.CREATED : CustomerEventType.UPDATED)))
        )
        .take(10)
        .delayElements(java.time.Duration.ofSeconds(2));

  }


}

enum CustomerEventType {
  CREATED, UPDATED
}

record CustomerEvent(Customer customer, CustomerEventType type) {



}

record Customer(Integer id, String name) {

}

record Order(Integer id, Integer customerId) {

}

