package com.transport.tracking.k.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@CrossOrigin(origins = {"http://tmsportal.tema-systems.com:8081", "http://tmsportal.tema-systems.com:8082"})
public class DefaultController {

    @RequestMapping ("/home")
    public String getHome() {
        return "index.html";
    }
}



