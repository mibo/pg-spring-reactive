package de.mirb.pg.spring.reactivespring.boundary;

import de.mirb.pg.spring.reactivespring.control.EventService;
import de.mirb.pg.spring.reactivespring.entity.Event;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
public class EventHandler {

  private final EventService eventService;

  EventHandler(EventService eventService) {
    this.eventService = eventService;
  }

  public Mono<ServerResponse> all(ServerRequest request) {
    Integer amount = getFirstHeaderAsInt(request);
    return ok().body(eventService.readEvents(amount), Event.class);
  }

  public Mono<ServerResponse> byId(ServerRequest request) {
    return ok().body(eventService.readEvent(request.pathVariable("id")), Event.class);
  }

  public Mono<ServerResponse> events(ServerRequest request) {
    return ok()
        .contentType(MediaType.TEXT_EVENT_STREAM)
        .body(eventService.infinite(), Event.class);
  }

  private String getFirstHeader(ServerRequest request) {
    ServerRequest.Headers headers = request.headers();
    List<String> amount = headers.header("amount");
    if (amount.isEmpty()) {
      return null;
    }
    return amount.get(0);
  }

  private Integer getFirstHeaderAsInt(ServerRequest request) {
    ServerRequest.Headers headers = request.headers();
    List<String> amount = headers.header("amount");
    if (amount.isEmpty()) {
      return null;
    }
    try {
      return Integer.parseInt(amount.get(0));
    } catch (NumberFormatException e) {
      return null;
    }
  }
}