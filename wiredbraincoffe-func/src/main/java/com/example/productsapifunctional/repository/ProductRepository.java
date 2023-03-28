package com.example.productsapifunctional.repository;


import com.example.productsapifunctional.Product;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ProductRepository  extends ReactiveMongoRepository<Product, String> {

}
