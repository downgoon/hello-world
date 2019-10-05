package hello;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class GreetingController {

    private final AtomicLong times = new AtomicLong();

    @RequestMapping(value = "/greeting")
    public Greeting greeting(
            @RequestParam(value = "name", defaultValue = "World")
                    String name) {



        return new Greeting(times.incrementAndGet(),
                String.format("Hello, %s!", name));
    }



    @RequestMapping(value = "/greeting2")
    public Greeting greeting(
            @RequestParam(value = "name", defaultValue = "World")
                    String name,
            HttpServletResponse response) {

        response.setStatus(HttpServletResponse.SC_CREATED);

        return new Greeting(times.incrementAndGet(),
                String.format("Hello, %s!", name));
    }


}
