import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class OperatorsTest {

  @Test
  void testMap() {
    Flux.range(1, 5)
        .log()
        .map(i -> i * 2)
        .subscribe(System.out::println);
  }

  @Test
  void testFlatMap() {
    Flux.range(1, 5)
        .log()
        .flatMap(i -> Flux.range(i * 10, 2))
        .subscribe(System.out::println);
  }

  @Test
  void FlatMapMany() {
    Mono.just(1)
        .log()
        .flatMapMany(i -> Flux.range(i * 10, 2))
        .subscribe(System.out::println);
  }

  @Test
  void zipTest() {
    Flux.zip(Flux.range(1, 5), Flux.range(6, 4))
        .log()
        .subscribe(System.out::println);
  }
}
