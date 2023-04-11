package com.example.softwaretesting.customer;

import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

@DataJpaTest(
    properties = {
        "spring.jpa.properties.jakarta.persistence.validation.mode=none"
    }
)
class CustomerRepositoryTest {

  @Autowired
  private CustomerRepository customerRepositoryUnderTest;

  @BeforeEach
  void setUp() {
    customerRepositoryUnderTest.saveAll(List.of(
        new Customer("John", "123456789"),
        new Customer("Jane", "987654321")
    ));
  }

  @Test
  void givenExistingPhoneNumberItShouldReturnCustomer() {

    BDDAssertions.then(customerRepositoryUnderTest.findCustomerByPhoneNumber("123456789"))
        .isPresent()
        .hasValueSatisfying(customer -> BDDAssertions.then(customer.getName()).isEqualTo("John"))
        .hasValueSatisfying(c -> BDDAssertions.assertThat(c.getId()).isNotNull());
  }

  @Test
  void givenNonExistingPhoneNumberItShouldReturnEmpty() {

    BDDAssertions.then(customerRepositoryUnderTest.findCustomerByPhoneNumber("000000000"))
        .isNotPresent();
  }

  @Test
  void itShouldSaveACustomer() {
    // given
    var c = new Customer("Ali", "3333");

    // when
    final Customer persistedCustomer = customerRepositoryUnderTest.saveAndFlush(c);

    // then
    BDDAssertions.then(persistedCustomer.getId()).isNotNull();
  }

  @Test
  void givenInvalidDataShouldThrowDataIntegrityException() {
    var c = new Customer(null, "333553");

    BDDAssertions.thenThrownBy(() -> customerRepositoryUnderTest.saveAndFlush(c))
        .isExactlyInstanceOf(DataIntegrityViolationException.class);

  }

//  @AfterEach
//  void tearDown() {
//    customerRepositoryUnderTest.deleteAllInBatch();
//  }
}