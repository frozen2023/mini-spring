package chen.springframework.controller;

import chen.springframework.web.bind.annotation.RequestMapping;
import chen.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @RequestMapping("/hello")
    public String printhello() {
        return "hello";
    }

}
