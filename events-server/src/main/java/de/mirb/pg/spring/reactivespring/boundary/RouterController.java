package de.mirb.pg.spring.reactivespring.boundary;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Component
public class RouterController {

  @Bean
  RouterFunction<?> routerFunction(EventHandler rh) {
    return route(GET("/events"), rh::all)
        .andRoute(GET("/events/{id}"), rh::byId)
        .andRoute(GET("/infinite"), rh::events);
  }
}