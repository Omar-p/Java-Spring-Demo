package org.example.airport;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
public class TicketKey implements Serializable {

  @Getter
  @Setter
  private String series;

  @Getter
  @Setter
  private String number;
}
