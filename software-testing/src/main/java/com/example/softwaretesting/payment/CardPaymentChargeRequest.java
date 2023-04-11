package com.example.softwaretesting.payment;

import java.math.BigDecimal;

public record CardPaymentChargeRequest(String cardSource,
                                       BigDecimal amount,
                                       Currency currency,
                                       String description) {
}
