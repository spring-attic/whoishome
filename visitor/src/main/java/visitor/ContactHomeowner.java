package visitor;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.web.client.RestTemplate;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.web.util.UriComponentsBuilder;

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
  Message checkIfAnybodyIsHome() {
    Message knockMessage = knock();

    return knockMessage;
  }

  Message nobodyHome() {
    return new Message("Visitor: Nobody seems to be Home.", true);
  }

  @HystrixCommand(fallbackMethod = "goAway")
  Message respondToMessage(String name) {
    Message greetingResponse = greetingResponse(name);

    return greetingResponse;
  }

  Message goAway(String name) {
    return new Message("Homeowner: Go away, " + name + "! I don't want any!", true);
  }

  private Message knock() {
    URI uri = UriComponentsBuilder.fromUriString("//homeowner/knock")
      .build()
      .toUri();

    return populateMessageFromUri(uri);
  }

  private Message greetingResponse(String name) {
    URI uri = UriComponentsBuilder.fromUriString("//homeowner/greeting")
      .queryParam("name", name)
      .build()
      .toUri();

    return populateMessageFromUri(uri);
  }

  private Message populateMessageFromUri(URI uri) {
    Message greeting = rest.getForObject(uri, Message.class);

    return greeting;
  }

}