package com.example.bootcampreactive;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.util.Assert;
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


	private final DatabaseClient databaseClient;
	private final CustomerRepository customerRepository;

	private final ReservationService reservationService;

	CustomerInitializer(DatabaseClient databaseClient, CustomerRepository customerRepository, ReservationService reservationService) {
		this.databaseClient = databaseClient;
		this.customerRepository = customerRepository;
		this.reservationService = reservationService;
	}


	@Override
	public void run(ApplicationArguments args) throws Exception {
		customerRepository.deleteAll()
				.thenMany(
						reservationService.saveAll("A", "B", "c", "D")
				)
				.thenMany(
						this.customerRepository.findAll()
				).subscribe(System.out::println);

//		this.databaseClient
//				.sql("select * from customer")
//				.fetch()
//				.all()
//				.doOnComplete(() -> System.out.println("done"))

	}
}

@Service
class ReservationService {
	private final TransactionalOperator transactionalOperator;
	private final CustomerRepository customerRepository;

	ReservationService(TransactionalOperator transactionalOperator, CustomerRepository customerRepository) {
		this.transactionalOperator = transactionalOperator;
		this.customerRepository = customerRepository;
	}

	Flux<Customer> saveAll(String... names) {
		return this.transactionalOperator
				.execute(status -> Flux.just(names)
						.map(name -> new Customer(null, name))
						.flatMap(customerRepository::save)
						.doOnNext(this::isValid)
				).thenMany(customerRepository.findAll()); // this is not transactional
	}

	private void isValid(Customer c) {
		Assert.isTrue(c.name() != null && Character.isUpperCase(c.name().charAt(0)), "The name must not be null and start with capital letter");

	}
}
