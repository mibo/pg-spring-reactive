package de.mirb.pg.spring.reactivespring.entity;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class EventRepository {

  private final Map<Integer, Event> id2events = Collections.synchronizedMap(new HashMap<>());
  private final List<Event> eventsStack = Collections.synchronizedList(new LinkedList<>());
  private final AtomicInteger idGenerator = new AtomicInteger(1);

  public Mono<Event> createEvent(String name) {
    Event e = new Event(idGenerator.getAndIncrement(), name);
    id2events.put(e.getId(), e);
    eventsStack.add(e);
    return Mono.just(e);
  }

  public Mono<Event> readEvent(String id) {
    try {
      return Mono.justOrEmpty(id2events.get(Integer.valueOf(id)));
    } catch (NumberFormatException e) {
      return Mono.empty();
    }
  }

  public Mono<Event> readEvent(Integer id) {
    return Mono.justOrEmpty(id2events.get(id));
  }

  public Flux<Event> consumeHeadEvents(int maxAmount) {
    int toConsume = maxAmount > eventsStack.size()? eventsStack.size(): maxAmount;
    List<Event> consumed = eventsStack.stream()
        .limit(toConsume)
        .collect(Collectors.toList());
    // TODO: check if this is correct
    consumed.forEach(e -> id2events.remove(e.getId()));
    eventsStack.removeAll(consumed);
    //
    return Flux.fromStream(consumed.stream());
  }

  public Flux<Event> listEvents(int maxAmount, int skip) {
    int size = eventsStack.size();
    if(skip > size) {
      return Flux.empty();
    }
    // reduce size with skipped amount
    size -= skip;
    int toConsume = maxAmount > size ? size : maxAmount;
    List<Event> consumed = eventsStack.stream()
        .skip(skip)
        .limit(toConsume)
        .collect(Collectors.toList());
    //
    return Flux.fromStream(consumed.stream());
  }
}
