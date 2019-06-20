package com.dejanristic.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BlogApplication {

    public static void main(String[] args) {
        System.setProperty("server.servlet.context-path", "/blog");
        SpringApplication.run(BlogApplication.class, args);
    }

}
