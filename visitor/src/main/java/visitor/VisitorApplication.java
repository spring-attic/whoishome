package visitor;

import java.net.URI;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
public class VisitorApplication {

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Autowired
    private RestTemplate rest;

    public static void main(String[] args) {
        SpringApplication.run(VisitorApplication.class, args);
    }

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String hello(@RequestParam(value="name", defaultValue="Bob") String name) {
        Greeting knockGreeting = knock();
        Greeting greetingResponse = greetingResponse(name);

        return knockGreeting.getMessage() + "<br/>" + greetingResponse.getMessage();
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
