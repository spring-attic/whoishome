package visitor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

@SpringBootApplication
@EnableDiscoveryClient
@Controller
public class VisitorApplication {

    @Autowired
    ContactHomeowner contactHomeowner;

    public static void main(String[] args) {
        SpringApplication.run(VisitorApplication.class, args);
    }

    @RequestMapping(value = "/knock", method = RequestMethod.GET)
    public String knock(Model model) {
      Conversation conversation = new Conversation("Visitor: 'Knock, knock.'");
      Message knockResponse = contactHomeowner.checkIfAnybodyIsHome();

      conversation.addContent(knockResponse.getMessage());
      model.addAttribute("conversation", conversation);
      model.addAttribute("failure", knockResponse.isCommunicationFailure());

      return "knock";
    }

    @RequestMapping(value = "/greeting", method = RequestMethod.POST)
    public String greeting(@RequestParam(value="visitorsName", defaultValue="Bob") String name, Model model) {
      Conversation conversation = new Conversation("Visitor: 'It's " + name + ". What's your favorite color?'");
      Message greetingResponse = contactHomeowner.respondToMessage(name);

      conversation.addContent(greetingResponse.getMessage());
      model.addAttribute("conversation", conversation);
      model.addAttribute("failure", greetingResponse.isCommunicationFailure());

      return "greeting";
    }

}