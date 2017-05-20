package de.mirb.pg.spring.reactivespring.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {
  private String name;
  private Date timestamp;

  public Event(String name) {
    this(name, new Date());
  }
}
