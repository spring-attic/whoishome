package visitor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
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
    public String knock(@RequestParam(value="name", defaultValue="Bob") String name, Model model) {
      String message = contactHomeowner.checkIfAnybodyIsHome(name);
      model.addAttribute("message", message);
      return "knock";
    }

}
