package de.mirb.pg.spring.reactivespring.boundary;

import de.mirb.pg.spring.reactivespring.control.EventService;
import de.mirb.pg.spring.reactivespring.entity.Event;
import lombok.Data;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Note that as of Spring 5.0.0.M5, @RequestMapping and RouterFunction cannot be mixed in the same application yet
 * (last checked at May 5, 2017).
 * To use/test this class the <code>@Component</code> annotation at the {@link RouterController} must be removed.
 */
@RestController
@RequestMapping("/classic")
public class ClassicWebController {

  EventService eventService;

  public ClassicWebController(EventService eventService) {
    this.eventService = eventService;
  }

  @GetMapping("/")
  public String welcome() {
    return "Hello World";
  }

  @GetMapping(path = "/sample/infinite", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<Event> listInfiniteEvents() {
    return eventService.infiniteEventStream();
  }

  @PostMapping(path = "/sample/infinite", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<Event> createInfiniteEvents() {
    return eventService.createInfiniteEventStream();
  }

  @PostMapping(path = "/events")
  public Mono<Event> createEvent(@RequestBody RequestEvent reqEvent) {
    return eventService.createEvent(reqEvent.getName());
  }

  @Data
  private static class RequestEvent {
    private String name;
  }

  @GetMapping(path = "/events/{id}")
  public Mono<Event> readEvent(@PathVariable("id") String id) {
    return eventService.readEvent(id);
  }

  @GetMapping(path = "/events")
  public Flux<Event> readEvents(
      @RequestHeader(value = RequestUtil.HEADER_AMOUNT, defaultValue = "10", required = false) int amount,
      @RequestHeader(value = RequestUtil.HEADER_SKIP, defaultValue = "0", required = false) int skip,
      @RequestHeader(value = RequestUtil.HEADER_CONSUME, defaultValue = "false", required = false) boolean consume) {

    if(consume) {
      return eventService.consumeEvents(amount);
    }
    return eventService.listEvents(amount, skip);
  }
}
