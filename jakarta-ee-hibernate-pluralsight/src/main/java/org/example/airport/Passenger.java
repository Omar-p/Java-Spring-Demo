package org.example.airport;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "PASSENGERS")
@Access(AccessType.FIELD)
@NoArgsConstructor
public class Passenger {

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "NAME")
  @Getter
  @Setter
  private String name;

  @ManyToOne
  @JoinColumn(name = "AIRPORT_ID")
  @Getter
  @Setter
  private Airport airport;

  @OneToMany(mappedBy = "passenger")
  private List<Ticket> tickets = new ArrayList<>();

  public Passenger(int id, String name) {
    this.id = id;
    this.name = name;
  }

  public List<Ticket> getTickets() {
    return Collections.unmodifiableList(tickets);
  }

  public void addTicket(Ticket ticket) {
    tickets.add(ticket);
  }
}
