package com.example.productapiannotation;

import com.example.productapiannotation.model.Product;
import com.example.productapiannotation.repository.ProductRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Flux;

import java.util.Random;
import java.util.stream.Stream;

@SpringBootApplication
public class WiredbraincoffeApplication {

	public static void main(String[] args) {
		SpringApplication.run(WiredbraincoffeApplication.class, args);
	}


	@Bean
	ApplicationRunner init(ProductRepository repository) {
		return args -> {
			repository.deleteAll()
					.thenMany(
			Flux.just("Espresso", "Cappuccino", "Latte", "Mocha", "Americano", "Macchiato")
					.map(name -> new Product(null, name, new Random().nextDouble() * 100))
					.flatMap(repository::save))
					.subscribe(System.out::println);
		};
	}
}
