package de.mirb.pg.spring.reactivespring.rest;

import de.mirb.pg.spring.reactivespring.domain.Event;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;

@RestController
public class WebController {

  @GetMapping("/")
  public String welcome() {
    return "Hello World";
  }

  @GetMapping(path = "/events")
  public Flux<Event> readEvents(@RequestHeader(value = "amount", defaultValue = "10", required = false) int amount) {
//    if(amount <= 0) {
//      return Flux.fromStream(eventStreamInfinite(10));
//    }
    return Flux.fromStream(eventStream(amount));
  }

  @GetMapping(path = "/infinite", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<Event> infinite() {
    Stream<Event> evStream = Stream.generate(() ->
        new Event(UUID.randomUUID().toString().substring(24), new Date()));
    Flux<Event> eventFlux = Flux.fromStream(evStream);
    Flux<Long> interval = Flux.interval(Duration.ofSeconds(1));

    return Flux.zip(eventFlux, interval).map(Tuple2::getT1);
  }

  @GetMapping(path = "/events/{id}")
  public Mono<Event> readEvent(@PathVariable("id") String id) {
    Mono<Event> response = Mono.just(new Event(id));

    return response;
  }

//  private Stream<Event> eventStreamInfinite(int amount) {
//    return Stream.generate(() -> new Event("Name.." + nextId(amount)));
//  }
//
//  private int counter = -1;
//  private int nextId(int amount) {
//    if(counter < 0) {
//      counter = amount;
//    } else if(counter == 0) {
//      return 0;
//    }
//    return counter--;
//  }

  private Stream<Event> eventStream(int amount) {
    Event[] events = new Event[amount];
    for (int i = 0; i < amount; i++) {
      events[i] = new Event("Event num: " + i);
    }
    return Stream.of(events);
  }
}
