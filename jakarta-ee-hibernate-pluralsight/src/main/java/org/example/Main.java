package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.example.airport.Airport;
import org.example.airport.Passenger;
import org.example.airport.Ticket;

public class Main {
  public static void main(String[] args) {

    final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("my-persistence-unit");
    final EntityManager em = entityManagerFactory.createEntityManager();

    em.getTransaction().begin();

    Airport airport = new Airport(1, "Heathrow");

    Passenger passenger = new Passenger(1, "John Smith");
    passenger.setAirport(airport);

    Passenger passenger2 = new Passenger(2, "Mary Smith");
    passenger2.setAirport(airport);

    Ticket ticket = new Ticket(1, "1234");
    passenger.addTicket(ticket);
    ticket.setPassenger(passenger);

    Ticket ticket2 = new Ticket(2, "5678");
    passenger.addTicket(ticket2);
    ticket2.setPassenger(passenger2);

    Ticket ticket3 = new Ticket(3, "9012");
    passenger2.addTicket(ticket3);
    ticket3.setPassenger(passenger2);


    em.persist(airport);
    em.persist(passenger);
    em.persist(passenger2);
    em.persist(ticket);
    em.persist(ticket2);
    em.persist(ticket3);

    em.getTransaction().commit();
    em.close();
  }
}