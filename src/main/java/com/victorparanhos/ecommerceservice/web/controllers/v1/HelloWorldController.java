package com.victorparanhos.ecommerceservice.web.controllers.v1;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1")
public class HelloWorldController {

    @RequestMapping(name = "/", produces = "application/json")
    public String home() {
        return "Hello Docker World";
    }

}
