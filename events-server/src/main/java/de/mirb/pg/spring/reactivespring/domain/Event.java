package de.mirb.pg.spring.reactivespring.domain;

import java.util.Date;

public class Event {
  final String name;
  final Date timestamp;

  public Event(String name, Date timestamp) {
    this.name = name;
    this.timestamp = timestamp;
  }

  public Event(String name) {
    this(name, new Date());
  }

  public String getName() {
    return name;
  }

  public Date getTimestamp() {
    return timestamp;
  }

  @Override
  public String toString() {
    return "Event{" +
        "name='" + name + '\'' +
        ", timestamp=" + timestamp +
        '}';
  }
}
