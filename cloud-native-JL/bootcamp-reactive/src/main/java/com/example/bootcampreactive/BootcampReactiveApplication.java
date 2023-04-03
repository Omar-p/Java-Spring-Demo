package com.example.bootcampreactive;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;

@SpringBootApplication
public class BootcampReactiveApplication {

	public static void main(String[] args) {
		SpringApplication.run(BootcampReactiveApplication.class, args);
	}


	@Bean
	RouterFunction<?> routes(CustomerRepository customerRepository) {
		return RouterFunctions
				.route()
				.path("/customers", builder -> builder
						.GET("", request -> {
							return ServerResponse.ok()
								.body(customerRepository.findAll(), Customer.class);
						})
				)
				.build();
	}


}

interface CustomerRepository extends ReactiveCrudRepository<Customer, String> {
}

@Component
class CustomerInitializer implements ApplicationRunner {


	private final CustomerRepository customerRepository;

	CustomerInitializer(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		customerRepository.deleteAll()
				.thenMany(
		Flux.just("omar", "hosam")
				.map(c -> new Customer(null, c))
				.flatMap(customerRepository::save)
				)
				.subscribe(System.out::println);
	}
}

