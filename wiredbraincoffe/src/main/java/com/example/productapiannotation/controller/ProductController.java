package com.example.productapiannotation.controller;

import com.example.productapiannotation.model.Product;
import com.example.productapiannotation.model.ProductEvent;
import com.example.productapiannotation.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.Duration;

@RestController
@RequestMapping("/products")
public class ProductController {

  private final ProductRepository repository;

  public ProductController(ProductRepository repository) {
    this.repository = repository;
  }

  @GetMapping
  Flux<Product> getAllProducts() {
    return repository.findAll();
  }

  @GetMapping("/{id}")
  Mono<ResponseEntity<Product>> getById(@PathVariable String id) {
    return repository.findById(id)
        .map(ResponseEntity::ok)
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  Mono<ResponseEntity<?>> createProduct(@RequestBody Product product) {
    return repository.save(product)
        .map(p -> ResponseEntity.created(URI.create("/products/" + p.getId())).build());
  }

  @PutMapping("/{id}")
  Mono<ResponseEntity<Product>> updateProduct(@PathVariable String id,@RequestBody Product product) {
    return repository.findById(id)
        .flatMap(existingProduct -> {
          existingProduct.setName(product.getName());
          existingProduct.setPrice(product.getPrice());
          return repository.save(existingProduct);
        })
        .map(ResponseEntity::ok)
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  Mono<ResponseEntity<Void>> deleteProduct(@PathVariable String id) {
    return repository.findById(id)
        .flatMap(existingProduct ->
            repository.delete(existingProduct)
                .then(Mono.just(ResponseEntity.ok().<Void>build()))
        )
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }


  @DeleteMapping
  Mono<Void> deleteAllProducts() {
    return repository.deleteAll();
  }


  @GetMapping(value = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  Flux<ProductEvent> getProductEvents() {
    return Flux.interval(Duration.ofSeconds(1))
        .map(val ->
            new ProductEvent(val, "Product Event")
        );
  }



}