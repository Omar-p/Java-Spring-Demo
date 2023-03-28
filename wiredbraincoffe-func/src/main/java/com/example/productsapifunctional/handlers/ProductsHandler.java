package com.example.productsapifunctional.handlers;

import com.example.productsapifunctional.Product;
import com.example.productsapifunctional.model.ProductEvent;
import com.example.productsapifunctional.repository.ProductRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.contentType;

@Component
public class ProductsHandler {

  private final ProductRepository productRepository;

  public ProductsHandler(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  public Mono<ServerResponse> getAllProducts(ServerRequest serverRequest) {
    final Flux<Product> all = productRepository.findAll();
    return ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(all, Product.class);
  }

  public Mono<ServerResponse> getProduct(ServerRequest serverRequest) {
    String productId = serverRequest.pathVariable("id");
    Mono<Product> product = productRepository.findById(productId);
    return product.flatMap(p -> ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(p))
        .switchIfEmpty(ServerResponse.notFound().build());
  }

  public Mono<ServerResponse> saveProduct(ServerRequest request) {
    Mono<Product> product = request.bodyToMono(Product.class);
    return ServerResponse.ok()
        .build(productRepository.saveAll(product).then());
  }

  public Mono<ServerResponse> deleteProductById(ServerRequest request) {
    String productId = request.pathVariable("id");
    Mono<Product> product = productRepository.findById(productId);
    return product.flatMap(
            p -> ServerResponse.ok()
                .build(productRepository.delete(p)))
        .switchIfEmpty(ServerResponse.notFound().build());
  }

  public Mono<ServerResponse> deleteAll(ServerRequest request) {
    return ServerResponse.ok()
        .build(productRepository.deleteAll());
  }

  public Mono<ServerResponse> updateProduct(ServerRequest request) {
    String productId = request.pathVariable("id");
    Mono<Product> product = request.bodyToMono(Product.class);
    var existingProduct = productRepository.findById(productId);
    return product.zipWith(existingProduct, (p, ep) -> {
      ep.setName(p.getName());
      ep.setPrice(p.getPrice());
      return ep;
    }).flatMap(p -> ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(productRepository.save(p), Product.class))
        .switchIfEmpty(ServerResponse.notFound().build());
  }

  public Mono<ServerResponse> getProductEvents(ServerRequest serverRequest) {
    Flux<ProductEvent> eventsFlux = Flux.interval(Duration.ofSeconds(1))
        .map(val ->
            new ProductEvent(val, "Product Event"));
    return ServerResponse.ok()
        .contentType(MediaType.TEXT_EVENT_STREAM)
        .body(eventsFlux, ProductEvent.class);
  }
}

@Configuration
class RouterFunctionConfig {

  @Bean
  RouterFunction<ServerResponse> routes(ProductsHandler handler) {
    return RouterFunctions.route()
        .path("/products",
            builder -> builder.nest(accept(MediaType.APPLICATION_JSON).or(contentType(MediaType.APPLICATION_JSON)).or(accept(MediaType.TEXT_EVENT_STREAM)),
                    nestedBuilder -> nestedBuilder
                        .GET("/events", handler::getProductEvents)
                        .GET("/{id}", handler::getProduct)
                        .PUT("/{id}", handler::updateProduct)
                        .POST(handler::saveProduct)
                        .GET(handler::getAllProducts)
                )
                .DELETE("/{id}", handler::deleteProductById)
                .DELETE(handler::deleteAll))
        .build();
  }


}
