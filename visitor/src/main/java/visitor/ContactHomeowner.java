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
  String checkIfAnybodyIsHome(String name) {
    Greeting knockGreeting = knock();
    Greeting greetingResponse = greetingResponse(name);

    return knockGreeting.getMessage() + "<br/>" + greetingResponse.getMessage();
  }

  String nobodyHome(String name) {
    return "Nobody seems to be Home. " + name + " out!";
  }

  private Greeting knock() {
    URI uri = UriComponentsBuilder.fromUriString("//homeowner/knock")
      .build()
      .toUri();

    Greeting greeting = rest.getForObject(uri, Greeting.class);

    return greeting;
  }

  private Greeting greetingResponse(String name) {
    URI uri = UriComponentsBuilder.fromUriString("//homeowner/greeting")
      .queryParam("name", name)
      .build()
      .toUri();

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
