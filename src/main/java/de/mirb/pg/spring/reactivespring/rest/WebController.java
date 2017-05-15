package de.mirb.pg.spring.reactivespring.rest;

import de.mirb.pg.spring.reactivespring.domain.Event;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.stream.Stream;

@RestController
public class WebController {

  @GetMapping("/")
  public String welcome() {
    return "Hello World";
  }

  @GetMapping(path = "/events")
  public Flux<Event> readEvents(@RequestHeader(value = "amount", defaultValue = "10", required = false) int amount) {
    Flux<Event> response = Flux.fromStream(eventStream(amount));

    return response;
  }

  @GetMapping(path = "/events/{id}")
  public Mono<Event> readEvent(@PathVariable("id") String id) {
    Mono<Event> response = Mono.just(new Event(id));

    return response;
  }

  private Stream<Event> eventStream(int amount) {
    Event[] events = new Event[amount];
    for (int i = 0; i < amount; i++) {
      events[i] = new Event("Event num: " + i);
    }
    return Stream.of(events);
  }
}
