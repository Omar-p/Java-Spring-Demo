package com.example.softwaretesting.customer;

import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;

@MockitoSettings
class CustomerRegistrationServiceTest {

  @Mock
  private CustomerRepository customerRepository;

  @InjectMocks
  private CustomerRegistrationService customerRegistrationService;

  @Captor
  private ArgumentCaptor<Customer> customerArgumentCaptor;

  @Test
  void itShouldRegisterNewCustomer() {
    // Given
    CustomerRegistrationRequest request = new CustomerRegistrationRequest("John", "123456789");
    // When
    BDDMockito.given(customerRepository.findCustomerByPhoneNumber(request.phoneNumber()))
        .willReturn(Optional.empty());


    customerRegistrationService.registerNewCustomer(request);

    BDDMockito.then(customerRepository).should().save(customerArgumentCaptor.capture());

    final Customer out = customerArgumentCaptor.getValue();
    final Customer expectedCustomer = new Customer(request.name(), request.phoneNumber());

    BDDAssertions.assertThat(out)
        .usingRecursiveComparison()
        .isEqualTo(expectedCustomer);


  }

  @Test
  void givenTakenPhoneNumberItShouldThrowIllegalStateException() {
    // Given
    CustomerRegistrationRequest request = new CustomerRegistrationRequest("John", "123456789");
    // When
    BDDMockito.given(customerRepository.findCustomerByPhoneNumber(request.phoneNumber()))
        .willReturn(Optional.of(new Customer(null, "Jane", "123456789")));

    // THEN
    BDDAssertions.assertThatThrownBy(() -> customerRegistrationService.registerNewCustomer(request))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("phone number taken");
    BDDMockito.then(customerRepository).should(never()).save(any());

  }

  @Test
  void givenExistingPhoneNumberButWithTheSameNameShouldDoNothing() {

    var r = new CustomerRegistrationRequest("John", "123456789");

    BDDMockito.given(customerRepository.findCustomerByPhoneNumber("123456789"))
        .willReturn(Optional.of(new Customer(UUID.randomUUID(), "John", "123456789")));

    customerRegistrationService.registerNewCustomer(r);

//    BDDMockito.verify(customerRepository, Mockito.never()).save(any());
    BDDMockito.then(customerRepository).should(never()).save(any());
    BDDMockito.then(customerRepository).should().findCustomerByPhoneNumber("123456789");
    BDDMockito.then(customerRepository).shouldHaveNoMoreInteractions();

  }
}