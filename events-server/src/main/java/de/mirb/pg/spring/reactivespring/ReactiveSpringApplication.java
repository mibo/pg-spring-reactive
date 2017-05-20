package de.mirb.pg.spring.reactivespring;

import de.mirb.pg.spring.reactivespring.domain.Event;
import de.mirb.pg.spring.reactivespring.rest.EventService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@SpringBootApplication
public class ReactiveSpringApplication {

  public static void main(String[] args) {
    SpringApplication.run(ReactiveSpringApplication.class, args);
  }

  @Bean
  RouterFunction<?> routerFunction(EventHandler rh) {
    return route(GET("/events"), rh::all)
        .andRoute(GET("/events/{id}"), rh::byId)
        .andRoute(GET("/infinite"), rh::events);
  }

  @Component
  class EventHandler {

    private final EventService eventService;

    EventHandler(EventService eventService) {
      this.eventService = eventService;
    }

    Mono<ServerResponse> all(ServerRequest request) {
      Integer amount = getFirstHeaderAsInt(request);
      return ok().body(eventService.readEvents(amount), Event.class);
    }

    Mono<ServerResponse> byId(ServerRequest request) {
      return ok().body(eventService.readEvent(request.pathVariable("id")), Event.class);
    }

    Mono<ServerResponse> events(ServerRequest request) {
      return ok()
          .contentType(MediaType.TEXT_EVENT_STREAM)
          .body(eventService.infinite(), Event.class);
    }

    private String getFirstHeader(ServerRequest request) {
      ServerRequest.Headers headers = request.headers();
      List<String> amount = headers.header("amount");
      if(amount.isEmpty()) {
        return null;
      }
      return amount.get(0);
    }

    private Integer getFirstHeaderAsInt(ServerRequest request) {
      ServerRequest.Headers headers = request.headers();
      List<String> amount = headers.header("amount");
      if(amount.isEmpty()) {
        return null;
      }
      try {
        return Integer.parseInt(amount.get(0));
      } catch (NumberFormatException e) {
        return null;
      }
    }
  }
}