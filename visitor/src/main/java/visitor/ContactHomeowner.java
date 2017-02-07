package visitor;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.web.client.RestTemplate;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Component
@EnableCircuitBreaker
public class ContactHomeowner {

  @Autowired
  private RestTemplate rest;

  @Bean
  @LoadBalanced
  public RestTemplate restTemplate() {
      return new RestTemplate();
  }

  @HystrixCommand(fallbackMethod = "nobodyHome")
  String checkIfAnybodyIsHome() {
    Greeting knockGreeting = knock();

    return knockGreeting.getMessage();
  }

  String nobodyHome() {
    return "Visitor: Nobody seems to be Home.";
  }

  @HystrixCommand(fallbackMethod = "goAway")
  String respondToGreeting(String name) {
    Greeting greetingResponse = greetingResponse(name);

    return greetingResponse.getMessage();
  }

  String goAway(String name) {
    return "Homeowner: Go away, " + name + "! I don't want any!";
  }

  private Greeting knock() {
    URI uri = UriComponentsBuilder.fromUriString("//homeowner/knock")
      .build()
      .toUri();

    return populateGreetingFromUri(uri);
  }

  private Greeting greetingResponse(String name) {
    URI uri = UriComponentsBuilder.fromUriString("//homeowner/greeting")
      .queryParam("name", name)
      .build()
      .toUri();

    return populateGreetingFromUri(uri);
  }

  private Greeting populateGreetingFromUri(URI uri) {
    Greeting greeting = rest.getForObject(uri, Greeting.class);

    return greeting;
  }

  private static class Greeting {

      private String message;

      @JsonCreator
      public Greeting(@JsonProperty("message") String message) {
          this.message = message;
      }

      public String getMessage() {
          return this.message;
      }

  }

}
