package com.example.coffeeshop.application.in;

import com.example.coffeeshop.application.order.Order;

import java.util.UUID;

public interface PreparingCoffee {
  Order startPreparingOrder(UUID orderId);
  Order finishPreparingOrder(UUID orderId);
}