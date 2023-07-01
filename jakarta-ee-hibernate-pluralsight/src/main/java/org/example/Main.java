package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.example.airport.Ticket;

public class Main {
  public static void main(String[] args) {

    final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("my-persistence-unit");
    final EntityManager em = entityManagerFactory.createEntityManager();

    em.getTransaction().begin();

    Ticket ticket = new Ticket();
    ticket.setSeries("AA");
    ticket.setNumber("1234567890");

    em.persist(ticket);

    em.getTransaction().commit();
    em.close();
  }
}