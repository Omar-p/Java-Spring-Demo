package org.example.airport;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "TICKETS")
@Access(AccessType.FIELD)
@NoArgsConstructor
public class Ticket {

  @Id
  @Column(name = "ID")
  private int id;

  @Getter
  @Setter
  @Column(name = "NUMBER")
  private String number;

  @ManyToOne
  @JoinColumn(name = "PASSENGER_ID")
  @Getter
  @Setter
  private Passenger passenger;

  public Ticket(int id, String number) {
    this.id = id;
    this.number = number;
  }
}
