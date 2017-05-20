package de.mirb.pg.spring.reactivespring.control;

import de.mirb.pg.spring.reactivespring.entity.Event;
import de.mirb.pg.spring.reactivespring.entity.EventRepository;
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

  EventRepository eventRepository;

  public EventService(EventRepository eventRepository) {
    this.eventRepository = eventRepository;
  }

  public Mono<Event> readEvent(String id) {
    return eventRepository.readEvent(id);
  }

  public Flux<Event> consumeEvents(int amount) {
    return eventRepository.consumeHead(amount);
  }

  public Flux<Event> infinite() {
    Stream<Event> evStream = Stream.generate(() ->
        eventRepository.createEvent(UUID.randomUUID().toString().substring(24)).block());
    Flux<Event> eventFlux = Flux.fromStream(evStream);
    Flux<Long> interval = Flux.interval(Duration.ofSeconds(1));

    return Flux.zip(eventFlux, interval).map(Tuple2::getT1);
  }

  private Stream<Event> eventStream(int amount) {
    Event[] events = new Event[amount];
    for (int i = 0; i < amount; i++) {
      events[i] = eventRepository.createEvent("Event num: " + i).block();
    }
    return Stream.of(events);
  }

}