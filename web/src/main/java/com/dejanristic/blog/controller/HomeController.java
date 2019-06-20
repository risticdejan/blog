package com.dejanristic.blog.controller;

import com.dejanristic.blog.util.UrlMappings;
import com.dejanristic.blog.util.ViewNames;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index() {
        return UrlMappings.REDIRECT_HOME;
    }

    @GetMapping(UrlMappings.HOME)
    public String home() {
        return ViewNames.HOME;
    }
}
