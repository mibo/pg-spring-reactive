package de.mirb.pg.spring.reactivespring.rest;

import de.mirb.pg.spring.reactivespring.domain.Event;
import de.mirb.pg.spring.reactivespring.domain.EventRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class EventService {

  public Mono<Event> readEvent(String id) {

    return Mono.just(new Event(id));
  }

  public Flux<Event> readEvents(int amount) {
    return Flux.fromStream(eventStream(amount));
  }

  public Flux<Event> infinite() {
    Stream<Event> evStream = Stream.generate(() ->
        new Event(UUID.randomUUID().toString().substring(24), new Date()));
    Flux<Event> eventFlux = Flux.fromStream(evStream);
    Flux<Long> interval = Flux.interval(Duration.ofSeconds(1));

    return Flux.zip(eventFlux, interval).map(Tuple2::getT1);
  }

  private Stream<Event> eventStream(int amount) {
    Event[] events = new Event[amount];
    for (int i = 0; i < amount; i++) {
      events[i] = new Event("Event num: " + i);
    }
    return Stream.of(events);
  }

}