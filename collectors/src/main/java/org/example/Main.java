package org.example;

import java.util.List;
import java.util.stream.Collectors;

public class Main {
  public static void main(String[] args) {

    var persons = List.of(
      new Person("Alice", 23),
      new Person("Bob", 38),
      new Person("Charlie", 18),
      new Person("Dave", 42),
      new Person("Eve", 25),
      new Person("Frank", 33),
      new Person("George", 52),
      new Person("Holly", 19)
    );

    System.out.println(
        persons.stream()
        .map(Person::age)
        .reduce(0, Integer::sum)
    );

    var collected = persons.stream()
        .collect(Collectors.toMap(Person::name, Person::age));

    System.out.println(collected);
    // reduce convert stream to something concrete
    // java has reduce in two forms: reduce, collect

    // the essence of OOP is Polymorphism

    // (Function Composition + lazy evaluation) is the essence of FP
    // without laziness we end up from a collection to another collection to another collection
    // and egar evaluation will end up with a lot of intermediate collections. more garbage will
    // affect performance.
    // lazy evaluation requires purity of functions.
    // pure function returns the same output for the same input any #times. - idempotency
    // Pure functions don't have side effects.
    // 1. Pure function do not change anything
    // 2. Pure function do not depend on anything that may change
    // ---
    // mutability is ok, but shared mutability is evil.
    /*
    reduce(
      accumulator,
      (accumulator, value) -> accumulator + value,
      // in case of parallel stream, how to combine the different accumulators
      (accumulator1, accumulator2) -> accumulator1 + accumulator2
    )
    u can delegate that work and use collect(Collectors.* )
     */
    // Collector<T, A, R>
    // T - type of the stream
    // A - type of the accumulator
    // R - type of the result
  }
}


record Person(String name, int age) {
  Person {
    if (age < 0) {
      throw new IllegalArgumentException("Age cannot be negative");
    }
  }
}