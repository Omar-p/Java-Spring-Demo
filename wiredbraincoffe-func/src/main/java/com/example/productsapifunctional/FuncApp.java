package com.example.productsapifunctional;

import com.example.productsapifunctional.repository.ProductRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.SimpleReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import reactor.core.publisher.Flux;

import java.util.Random;



@SpringBootApplication
public class FuncApp {

  public static void main(String[] args) {
    SpringApplication.run(FuncApp.class, args);
  }


  @Bean
  ApplicationRunner init(ProductRepository repository) {
    /**
     * type mapper write wrong type in _class (com.example.productsapifunctional.FuncApp$1) instead of (com.example.productsapifunctional.Product)
     */
    return args -> {
      repository.deleteAll()
          .thenMany(
              Flux.just("Espresso", "Cappuccino", "Latte", "Mocha", "Americano", "Macchiato")
                  .map(name -> new Product(null, name, new Random().nextDouble() * 100) {
                  })
                  .flatMap(repository::save))
          .subscribe(System.out::println);
    };
  }

  /**
   *
   * remove _class from mongo document because it was wrong. I DON'T KNOW WHY.
   */
  @Bean
  ReactiveMongoTemplate reactiveMongoTemplate(SimpleReactiveMongoDatabaseFactory factory,  MappingMongoConverter converter) {
    converter.setTypeMapper(new DefaultMongoTypeMapper(null));
    return new ReactiveMongoTemplate(factory, converter);
  }
}
