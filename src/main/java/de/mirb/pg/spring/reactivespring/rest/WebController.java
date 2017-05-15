package de.mirb.pg.spring.reactivespring.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebController {

  @GetMapping("/")
  public String welcome() {
    return "Hello World";
  }
}
