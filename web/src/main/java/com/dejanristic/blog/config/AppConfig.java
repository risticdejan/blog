package com.dejanristic.blog.config;

import com.dejanristic.blog.util.PerPage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:app.properties")
public class AppConfig {

    @Value("${page.perPage:4}")
    private int perPage;

    // == bean methods ==
    @Bean
    @PerPage
    public int perPage() {
        return perPage;
    }
}
