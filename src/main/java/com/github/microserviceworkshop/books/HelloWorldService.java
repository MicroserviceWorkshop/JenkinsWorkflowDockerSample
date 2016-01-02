package com.github.microserviceworkshop.books;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by marco.poli on 31.08.2015.
 */
@RestController
public class HelloWorldService {

    @RequestMapping("/echo")
    public String echo(@RequestParam(value="name", defaultValue="World") String input) {
        return "Hello " + input;
    }

}
