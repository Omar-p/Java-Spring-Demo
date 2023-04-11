package com.example.softwaretesting.payment;

import com.example.softwaretesting.customer.Customer;
import com.example.softwaretesting.customer.CustomerRepository;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

@DataJpaTest(
    properties = {
        "spring.jpa.properties.jakarta.persistence.validation.mode=none"
    }
)
class PaymentRepositoryTest {

  @Autowired
  PaymentRepository paymentRepositoryUnderTest;

  @Autowired
  CustomerRepository customerRepository;

  Customer persistedCustomer;

  @BeforeEach
  void setUp() {
    persistedCustomer = customerRepository.saveAndFlush(new Customer("John", "123456789"));
  }

  @Test
  void itShouldSaveAPayment() {
    // given
    var p = new Payment(null, persistedCustomer, BigDecimal.TEN, Currency.USD, "CD", "123" );

    // when
    final Payment persistedPayment = paymentRepositoryUnderTest.saveAndFlush(p);

    // then
    BDDAssertions.then(persistedPayment.getPaymentId()).isNotNull();

    BDDAssertions.assertThat(persistedPayment.getCustomer()).usingRecursiveComparison()
        .isEqualTo(persistedCustomer);
  }
}