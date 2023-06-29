package com.example.edge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@SpringBootApplication
public class EdgeApplication {

	public static void main(String[] args) {
		SpringApplication.run(EdgeApplication.class, args);
	}

}

@Configuration
class GatewayConfiguration {

	@Bean
	RouteLocator gateway(RouteLocatorBuilder rlb) {

		return rlb.routes()
				.route(rs -> rs.path("/proxy").and().host("*.spring.io")
						.filters(fs -> fs.setPath("/customers")
								.addResponseHeader("X-TestHeader", "gateway-proxy")
								.addResponseHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*"))
						.uri("http://localhost:8080/")).build();

	}


}