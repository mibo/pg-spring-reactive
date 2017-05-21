package de.mirb.pg.spring.reactive.event.client;

import de.mirb.pg.spring.reactive.event.client.entity.Event;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.Disposable;

@SpringBootApplication
public class ReactiveEventClientApp implements CommandLineRunner {

  public static void main(String[] args) {
//    new SpringApplicationBuilder().web(WebApplicationType.NONE).
    SpringApplication.run(ReactiveEventClientApp.class, args);

//    ConfigurableApplicationContext ctx = SpringApplication.run(ReactiveEventClientApp.class, args);
//    ctx.setEnvironment(new StandardEnvironment().set);
  }

//  @Bean
//  CommandLineRunner infinite() {
//    return args -> {
//      WebClient.create("http://localhost:8080")
//          .get()
//              .uri("sample/infinite")
//              .retrieve()
//              .bodyToFlux(Event.class)
//              .subscribe(System.out::println);
//    };
//  }

  @Override
  public void run(String... strings) throws Exception {
    WebClient webClient = WebClient.create("http://localhost:8080");
//    Disposable response =
        webClient.get()
            .uri("/sample/infinite")
            .retrieve()
            .bodyToFlux(Event.class)
            .subscribe(System.out::println);
//    response.dispose();
    System.out.println("Started");
  }
}