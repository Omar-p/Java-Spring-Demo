package com.example.coffeeshop.application.in;

import com.example.coffeeshop.application.order.Order;
import com.example.coffeeshop.application.payment.CreditCard;
import com.example.coffeeshop.application.payment.Payment;

import java.util.UUID;

public interface OrderingCoffee {
  Order placeOrder(Order order);

  Order updateOrder(UUID orderId, Order order);



  void cancelOrder(UUID orderId);

  Payment payOrder(UUID orderId, CreditCard creditCard);

  Receipt readReceipt(UUID orderId);

  Order takeOrder(UUID orderId);
}
