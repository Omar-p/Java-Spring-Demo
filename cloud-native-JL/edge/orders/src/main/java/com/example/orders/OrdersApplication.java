package com.example.orders;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.LongStream;

@SpringBootApplication
public class OrdersApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(OrdersApplication.class, args);
		System.in.read();
	}

}

record Order(Long id, Long customerId) {}
@Controller
class OrderController {
	private final Map<Long, Collection<Order>> db = new ConcurrentHashMap<>();

	OrderController() {
		LongStream.range(1, 8)
			.forEach(id -> {
				var max = (long) (Math.random() * 100);
				this.db.put(id, LongStream.range(1, max)
					.mapToObj(orderId -> new Order(orderId, id))
					.toList());
			});
	}

	@MessageMapping("orders.{customerId}")
	Flux<Order> ordersFor(@DestinationVariable Long customerId) {
		return Flux.fromIterable(this.db.get(customerId));
	}
}
