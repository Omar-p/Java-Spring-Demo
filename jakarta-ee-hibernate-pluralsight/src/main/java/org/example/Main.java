package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.example.airport.Passenger;

public class Main {
  public static void main(String[] args) {

    final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("my-persistence-unit");
    final EntityManager em = entityManagerFactory.createEntityManager();

    em.getTransaction().begin();

    Passenger passenger = new Passenger(1, "John Smith");
    passenger.setAddress("123 Main Street");
    passenger.setZipCode("12345");
    passenger.setCity("New York");
    passenger.setAreaCode("123");
    passenger.setLineNumber("4567890");
    passenger.setPrefix("1");
    em.persist(passenger);

    em.getTransaction().commit();
    em.close();
  }
}