package com.example.springweb;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HelloController {

    @RequestMapping("/")
    public String index() {
        /*ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        return modelAndView;*/
        return "index.html";
    }

}
