package com.dejanristic.blog.controller;

import com.dejanristic.blog.util.UrlMappings;
import com.dejanristic.blog.util.ViewNames;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PostController {

    @GetMapping(UrlMappings.POST)
    public String index() {
        return ViewNames.POST;
    }
}
