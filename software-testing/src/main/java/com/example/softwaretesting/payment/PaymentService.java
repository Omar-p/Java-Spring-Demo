package com.example.softwaretesting.payment;

import com.example.softwaretesting.customer.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

  private final PaymentRepository paymentRepository;

  private final CustomerRepository customerRepository;

  private final CardPaymentCharger cardPaymentCharger;

  void chargeCard(PaymentRequest paymentRequest) {
    if (customerRepository.findById(paymentRequest.customerId()).isEmpty()) {
      throw new IllegalStateException("Customer not found");
    }
    paymentCharge(paymentRequest);
    persistPayment(paymentRequest);

    // TODO: send sms
  }

  private void persistPayment(PaymentRequest paymentRequest) {
    Payment payment = toPayment(paymentRequest);
    paymentRepository.save(payment);
  }

  private void paymentCharge(PaymentRequest paymentRequest) {
    var cardPaymentChargeRequest = toPaymentChargeRequest(paymentRequest);
    var cardPaymentCharge = cardPaymentCharger.chargeCard(cardPaymentChargeRequest);
    if (!cardPaymentCharge.isCardDebited()) {
      throw new IllegalStateException("Card not debited");
    }
  }

  private CardPaymentChargeRequest toPaymentChargeRequest(PaymentRequest paymentRequest) {
    return new CardPaymentChargeRequest(
        paymentRequest.source(),
        paymentRequest.amount(),
        paymentRequest.currency(),
        paymentRequest.description()
    );
  }

  private Payment toPayment(PaymentRequest paymentRequest) {
    Payment payment = new Payment();
    payment.setCustomer(customerRepository.findById(paymentRequest.customerId()).get());
    payment.setAmount(paymentRequest.amount());
    payment.setCurrency(paymentRequest.currency());
    payment.setSource(paymentRequest.source());
    payment.setDescription(paymentRequest.description());
    return payment;
  }
}
