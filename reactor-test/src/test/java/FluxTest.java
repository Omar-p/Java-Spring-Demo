import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

public class FluxTest {

  @Test
  void testFlux() {
    Flux<String> flux = Flux.just("Hello", "World");
    StepVerifier.create(flux)

        .expectNext("Hello")
        .expectNext("World")
        .verifyComplete();
  }

  @Test
  void fluxRange() {
    Flux<Integer> flux = Flux.range(1, 5).log();
    StepVerifier.create(flux)
        .expectNext(1, 2, 3, 4, 5)
        .verifyComplete();
  }


  @Test
  void fluxFromInterval() throws InterruptedException {
    Flux.interval(Duration.ofSeconds(1))
        .take(5).log()
        .subscribe(System.out::println);
    Thread.sleep(8000);
  }
}
