package de.mirb.pg.spring.reactivespring.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {
  private Integer id;
  private String name;
  private Date timestamp;

  public Event(Integer id, String name) {
    this(id, name, new Date());
  }
}
