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
@SecondaryTables(
    {
        @SecondaryTable(
            name = "ADDRESSES",
            pkJoinColumns = @PrimaryKeyJoinColumn(name = "ID", referencedColumnName = "PASSENGER_ID")
        ),
        @SecondaryTable(
            name = "PHONES",
            pkJoinColumns = @PrimaryKeyJoinColumn(name = "ID", referencedColumnName = "PASSENGER_ID")
        )
    }
)
@Access(AccessType.FIELD)
@NoArgsConstructor
public class Passenger {

  @Id
  @Getter
  @Column(name = "PASSENGER_ID")
  private int id;

  @Column(name = "NAME")
  @Getter
  @Setter
  private String name;

  @Column(name = "PASSENGER_ADDRESS", table = "ADDRESSES", columnDefinition = "VARCHAR(25) NOT NULL")
  @Getter
  @Setter
  private String address;

  @Column(name ="ZIP_CODE", table = "ADDRESSES", columnDefinition = "VARCHAR(5) NOT NULL")
  @Getter
  @Setter
  private String zipCode;

  @Column(name ="CITY", table = "ADDRESSES", columnDefinition = "VARCHAR(25) NOT NULL")
  @Getter
  @Setter
  private String city;

  @Column(name = "AREA_CODE", table = "PHONES", columnDefinition = "VARCHAR(5) NOT NULL")
  @Getter
  @Setter
  private String areaCode;


  @Column(name = "PREFIX", table = "PHONES", columnDefinition = "VARCHAR(5) NOT NULL")
  @Getter
  @Setter
  private String prefix;

  @Column(name = "LINE_NUMBER", table = "PHONES", columnDefinition = "VARCHAR(10) NOT NULL")
  @Getter
  @Setter
  private String lineNumber;

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
