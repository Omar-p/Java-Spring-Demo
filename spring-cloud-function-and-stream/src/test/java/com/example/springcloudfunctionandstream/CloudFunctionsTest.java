package com.example.springcloudfunctionandstream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.function.context.FunctionCatalog;
import org.springframework.cloud.function.context.test.FunctionalSpringBootTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.function.Function;

@FunctionalSpringBootTest
class CloudFunctionsTest {

  @Autowired
  private Functions functions;

  /**
   * This is the catalog of all the functions that are available in the application.
   * You can use this to get a reference to a function by name.
   * This is useful when you want to test a function that is not a bean.
   * For example, if you have a function that is a lambda, you can't use @Bean to
   * create a bean for it. In that case, you can use the catalog to get a reference
   * to the function.
   */
  @Autowired
  private FunctionCatalog catalog;

  @Test
  void  testUppercaseThenReverse() {
    var input = "spring cloud";
    var expected = "DUOLC GNIRPS";
    final Function<Flux<String>, Flux<String>> f = catalog.lookup(Function.class, "uppercase|reverseReactive");

    StepVerifier.create(f.apply(Flux.just(input)))
        .expectNext(expected)
        .verifyComplete();

  }
}
