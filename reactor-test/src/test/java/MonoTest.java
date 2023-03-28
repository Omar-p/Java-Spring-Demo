import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class MonoTest {

    @Test
    public void testMono() {
        Mono<String> mono = Mono.just("Hello");
        StepVerifier.create(mono)
                .expectNext("Hello")
                .verifyComplete();
    }

    @Test
    public void testMonoLog() {
        Mono.just("Hello")
            .doOnSubscribe(subscription -> System.out.println("Subscribed"))
            .doOnRequest(longNumber -> System.out.println("Request received, starting doing something..."))
            .doOnSuccess(s -> System.out.println("Done!"))
                .log()
                .subscribe();

    }

    @Test
    void testOnErrorResume() {
        Mono<Object> error = Mono.error(new RuntimeException("Exception Occurred"))
                .onErrorResume(e -> {
                    System.out.println("Inside On Error Resume");
                    return Mono.just("default");
                });

        StepVerifier.create(error.log())
                .expectNext("default")
                .verifyComplete();
    }
}
