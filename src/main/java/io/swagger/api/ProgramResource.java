package io.swagger.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ProgramResource {

    @RequestMapping({"/hello"})
    public String hello() {
        return "Hellow World";
    }
}
