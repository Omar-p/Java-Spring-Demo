package com.example.softwaretesting.payment;

import com.example.softwaretesting.customer.Customer;
import com.example.softwaretesting.customer.CustomerRepository;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


class PaymentServiceTest {

  @Mock
  private CustomerRepository customerRepository;

  @Mock
  private PaymentRepository paymentRepository;
  @Mock
  private CardPaymentCharger cardPaymentCharger;

  private PaymentService underTest;

  private AutoCloseable autoCloseable;

  @BeforeEach
  void setUp() {
    autoCloseable = MockitoAnnotations.openMocks(this);
    underTest = new PaymentService(paymentRepository, customerRepository, cardPaymentCharger);
  }

  @Test
  void itShouldChargeCardSuccessfully() {
    var customerId = UUID.randomUUID();
    given(customerRepository.findById(customerId))
        .willReturn(Optional.of(mock(Customer.class)));

    given(cardPaymentCharger.chargeCard(BDDMockito.any()))
        .willReturn(new CardPaymentCharge(true));

    // when
    underTest.chargeCard(new PaymentRequest(customerId, null, null, null, null));

    // then
    ArgumentCaptor<Payment> payment = ArgumentCaptor.forClass(Payment.class);
    verify(paymentRepository).save(payment.capture());


  }

  @Test
  void itShouldNotChargeCardAndThrowWhenCustomerNotFound() {
    // given
    var customerId = UUID.randomUUID();
    given(customerRepository.findById(customerId))
        .willReturn(Optional.empty());

    // when
    BDDAssertions.assertThatThrownBy(
        () -> underTest.chargeCard(new PaymentRequest(customerId, null, null, null, null))
    ).isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("Customer not found");
    // then

    verifyNoInteractions(cardPaymentCharger);
    verifyNoInteractions(paymentRepository);

  }

  @Test
  void itShouldNotChargeCardAndThrowWhenCardIsNotDebited() {
    // given
    var customerId = UUID.randomUUID();
    given(customerRepository.findById(customerId))
        .willReturn(Optional.of(mock(Customer.class)));

    given(cardPaymentCharger.chargeCard(BDDMockito.any()))
        .willReturn(new CardPaymentCharge(false));

    // when
    BDDAssertions.assertThatThrownBy(
        () -> underTest.chargeCard(new PaymentRequest(customerId, null, null, null, null))
    ).isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("Card not debited");
    // then

    verify(paymentRepository, never()).save(BDDMockito.any());
  }

  @AfterEach
  void tearDown() throws Exception {
    autoCloseable.close();
  }

}