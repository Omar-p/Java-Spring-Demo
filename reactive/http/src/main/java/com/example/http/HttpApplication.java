package com.example.http;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.stream.Stream;

@SpringBootApplication
public class HttpApplication {

	public static void main(String[] args) {
		SpringApplication.run(HttpApplication.class, args);
	}

	@Bean
	RouterFunction<ServerResponse> routes(GreetingService greetingService) {
		return RouterFunctions.route()
				.GET("/greetingfunc/{name}", request -> {
					String name = request.pathVariable("name");
					return ServerResponse.ok().body(greetingService.greet(new GreetingRequest(name)), GreetingResponse.class);
				})
				.GET("/greetingfuncmany/{name}", request -> {
					String name = request.pathVariable("name");
					return ServerResponse.ok().contentType(MediaType.TEXT_EVENT_STREAM).body(greetingService.greetMany(new GreetingRequest(name)), GreetingResponse.class);
				})
				.build();
	}
}

@RestController
class GreetingController {

	private final GreetingService greetingService;

	GreetingController(GreetingService greetingService) {
		this.greetingService = greetingService;
	}

	@GetMapping("/greeting/{name}")
	Mono<GreetingResponse> greet(@PathVariable("name") String name) {
		return greetingService.greet(new GreetingRequest(name));
	}



}

@Service
class GreetingService {

	private GreetingResponse greet(String name) {
		return new GreetingResponse("Hello, " + name + ", " + LocalDateTime.now().toString());
	}

	Mono<GreetingResponse> greet(GreetingRequest greetingRequest) {
		return Mono.just(greet(greetingRequest.getName()));
	}

	Flux<GreetingResponse> greetMany(GreetingRequest greetingRequest) {
		return Flux.fromStream(
				Stream.generate(
						() -> greet(greetingRequest.getName())
				)
		).delayElements(java.time.Duration.ofSeconds(2));
	}

}


@Data @AllArgsConstructor @NoArgsConstructor
class GreetingResponse {
	private String message;
}

@Data @AllArgsConstructor @NoArgsConstructor
class GreetingRequest {
	private String name;
}



