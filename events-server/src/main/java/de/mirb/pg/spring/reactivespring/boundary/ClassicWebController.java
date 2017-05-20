package de.mirb.pg.spring.reactivespring.boundary;

import de.mirb.pg.spring.reactivespring.entity.Event;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * Note that as of Spring 5.0.0.M5, @RequestMapping and RouterFunction cannot be mixed in the same application yet
 * (last checked at May 5, 2017).
 * To use/test this class the <code>@Component</code> annotation at the {@link RouterController} must be removed.
 */
@RestController
@RequestMapping("/classic")
public class ClassicWebController {

  @GetMapping("/")
  public String welcome() {
    return "Hello World";
  }

  @GetMapping(path = "/events")
  public Flux<Event> readEvents(@RequestHeader(value = "amount", defaultValue = "10", required = false) int amount) {
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
    return Mono.just(new Event(id));
  }

  private Stream<Event> eventStream(int amount) {
    Event[] events = new Event[amount];
    for (int i = 0; i < amount; i++) {
      events[i] = new Event("Event num: " + i);
    }
    return Stream.of(events);
  }
}
