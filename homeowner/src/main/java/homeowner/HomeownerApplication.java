package homeowner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
public class HomeownerApplication {

    private Log log = LogFactory.getLog(HomeownerApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(HomeownerApplication.class, args);
    }

    @RequestMapping("/knock")
    public Greeting knock() {
      log.info("Respond to knock");
      return new Greeting();
    }

    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name") String name) {
      log.info(String.format("Respond to %s", name));
      return new Greeting(name);
    }

    private static class Greeting {

        private static final String TEMPLATE = "Homeowner: 'I don't want any! Get off my lawn, %s!'";
        private static final String DEFAULT_GREETING = "Homeowner: 'Who's there?'";

        private String message;

        public Greeting() {
          this.message = DEFAULT_GREETING;
        }

        public Greeting(String name) {
            this.message = String.format(TEMPLATE, name);
        }

        public String getMessage() {
            return this.message;
        }

    }

}
