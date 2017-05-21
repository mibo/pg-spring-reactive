package de.mirb.pg.spring.reactive.event.client.entity;

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
}
