package com.example.springcloudfunctionandstream;

import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FunctionsTest {

  private final Functions functions = new Functions();
  @Test
  void itShouldUppercase() {
    String input = "spring cloud";

    var expected = "SPRING CLOUD";
    var actual = functions.uppercase().apply(input);

    BDDAssertions.assertThat(actual).isEqualTo(expected);
  }

  @Test
  void itShouldReverse() {
    String input = "spring cloud";

    var expected = "duolc gnirps";
    var actual = functions.reverse().apply(input);

    BDDAssertions.assertThat(actual).isEqualTo(expected);
  }

  @Test
  void itShouldUppercaseAndReverse() {
    String input = "spring cloud";

    var expected = "DUOLC GNIRPS";
    var actual = functions.reverse()
        .andThen(functions.uppercase())
        .apply(input);

    BDDAssertions.assertThat(actual).isEqualTo(expected);
  }
}