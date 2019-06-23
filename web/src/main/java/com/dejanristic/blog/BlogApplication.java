package com.dejanristic.blog;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class BlogApplication implements CommandLineRunner {

    private DBData data;

    @Autowired
    public BlogApplication(DBData data) {
        this.data = data;
    }

    public static void main(String[] args) {
        System.setProperty("server.servlet.context-path", "/blog");
        SpringApplication.run(BlogApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        this.data.load();
    }

}
