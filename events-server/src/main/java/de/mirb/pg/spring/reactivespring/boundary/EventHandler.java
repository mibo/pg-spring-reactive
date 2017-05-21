package de.mirb.pg.spring.reactivespring.boundary;

import de.mirb.pg.spring.reactivespring.control.EventService;
import de.mirb.pg.spring.reactivespring.entity.Event;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

import static de.mirb.pg.spring.reactivespring.boundary.RequestUtil.*;
import static org.springframework.web.reactive.function.server.ServerResponse.created;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
public class EventHandler {

  private final EventService eventService;

  EventHandler(EventService eventService) {
    this.eventService = eventService;
  }

  public Mono<ServerResponse> createEvent(ServerRequest request) {
    Mono<Event> reqEvent = request.bodyToMono(Event.class);
    Mono<Event> createdEvent = eventService.createEvent(reqEvent.block().getName());
    return created(URI.create("events/" + createdEvent.block().getId()))
        .body(createdEvent, Event.class);
  }

  public Mono<ServerResponse> readEvents(ServerRequest request) {
    Integer amount = getFirstHeaderAsInt(request, HEADER_AMOUNT, 10);
    Boolean consume = getFirstHeaderAsBoolean(request, HEADER_CONSUME, false);
    if(consume) {
      return ok().body(eventService.consumeEvents(amount), Event.class);
    }
    Integer skip = getFirstHeaderAsInt(request, HEADER_SKIP, 0);
    return ok().body(eventService.listEvents(amount, skip), Event.class);
  }

  public Mono<ServerResponse> readEventById(ServerRequest request) {
    return ok().body(eventService.readEvent(request.pathVariable("id")), Event.class);
  }

  public Mono<ServerResponse> createInfiniteEvents(ServerRequest request) {
    return created(URI.create("events"))
        .contentType(MediaType.TEXT_EVENT_STREAM)
        .body(eventService.createInfiniteEventStream(), Event.class);
  }


  public Mono<ServerResponse> infiniteEvents(ServerRequest request) {
    return ok()
        .contentType(MediaType.TEXT_EVENT_STREAM)
        .body(eventService.infiniteEventStream(), Event.class);
  }
}