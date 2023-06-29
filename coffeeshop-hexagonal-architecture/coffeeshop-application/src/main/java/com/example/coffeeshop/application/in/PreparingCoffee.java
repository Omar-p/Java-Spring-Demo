package com.example.coffeeshop.application.in;

import java.util.UUID;

public interface PreparingCoffee {
  Order startPreparingOrder(UUID orderId);
  Order finishPreparingOrder(UUID orderId);
}