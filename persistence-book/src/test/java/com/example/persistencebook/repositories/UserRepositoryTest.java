package com.example.persistencebook.repositories;

import com.example.persistencebook.model.User;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.ZoneId;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserRepositoryTest {
  @Autowired
  private UserRepository userRepository;

  @BeforeAll
  void setUp() {
    userRepository.deleteAll();
    Faker faker = new Faker();
    for (int i = 0; i < 10; i++) {
      var user = new User(faker.name().username());
      user.setActive(true);
      user.setEmail(faker.internet().emailAddress());
      user.setLevel(faker.number().numberBetween(1, 10));
      user.setRegistrationDate(
          faker.date().birthday()
              .toInstant()
              .atZone(ZoneId.systemDefault())
              .toLocalDate()
      );
      userRepository.save(user);
    }
  }

  @Test
  void testFindAll() {
    List<User> users = userRepository.findAll();
    assertEquals(10, users.size());
  }

  @Test
  void testFindUser() {
    var u = userRepository.findAll().get(0).getUsername();
    User r = userRepository.findByUsername(u);
    assertEquals(u, r.getUsername());
  }

  @Test
  void testOrder() {
    User user1 = userRepository.findFirstByOrderByUsernameAsc();
    User user2 = userRepository.findTopByOrderByRegistrationDateDesc();
    Page<User> userPage = userRepository.findAll(PageRequest.of(1, 3));
    List<User> users = userRepository.findFirst2ByLevel(1,
        Sort.by("registrationDate"));

    assertAll(

        () -> assertNotNull(users),
        () -> assertEquals(3, userPage.getSize())

    );
  }
  @AfterAll
  void afterAll() {
    userRepository.deleteAll();
  }
}