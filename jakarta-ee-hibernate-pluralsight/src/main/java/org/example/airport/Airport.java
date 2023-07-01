package org.example.airport;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "AIRPORTS")
@Access(AccessType.FIELD)
@NoArgsConstructor
public class Airport {

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "NAME")
  @Getter
  @Setter
  private String name;

  @OneToMany(mappedBy = "airport")
  private List<Passenger> passengers = new ArrayList<>();
  public Airport(int id, String name) {
    this.id = id;
    this.name = name;
  }

  public List<Passenger> getPassengers() {
    return Collections.unmodifiableList(passengers);
  }

  public void addPassenger(Passenger passenger) {
    passengers.add(passenger);
  }
}
