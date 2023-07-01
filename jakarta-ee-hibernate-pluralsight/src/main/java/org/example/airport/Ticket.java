package org.example.airport;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "TICKETS")
@Access(AccessType.FIELD)
@NoArgsConstructor
@IdClass(TicketKey.class)
public class Ticket {


  @Getter
  @Setter
  @Id
  private String series;

  @Getter
  @Setter
  @Id
  private String number;


  @ManyToOne
  @JoinColumn(name = "PASSENGER_ID")
  @Getter
  @Setter
  private Passenger passenger;


}
