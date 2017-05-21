package de.mirb.pg.spring.reactivespring.boundary;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * Note that as of Spring 5.0.0.M5, @RequestMapping and RouterFunction cannot be mixed in the same application yet
 * (last checked at May 5, 2017).
 */
@Component
public class RouterController {

  @Bean
  RouterFunction<?> routerFunction(EventHandler handler) {
    return route(GET("/events"), handler::readEvents)
        .andRoute(GET("/events/{id}"), handler::readEventById)
        .andRoute(POST("/events"), handler::createEvent)
        .andRoute(POST("/sample/infinite"), handler::createInfiniteEvents)
        .andRoute(GET("/sample/infinite"), handler::infiniteEvents);
  }
}