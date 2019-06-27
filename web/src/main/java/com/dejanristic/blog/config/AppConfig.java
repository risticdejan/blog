package com.dejanristic.blog.config;

import com.dejanristic.blog.interceptor.RequestInterceptor;
import com.dejanristic.blog.util.PerPage;
import com.dejanristic.blog.util.PerPageAdmin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@PropertySource("classpath:app.properties")
public class AppConfig implements WebMvcConfigurer {

    @Value("${page.perPage:4}")
    private int perPage;

    @Value("${page.perPageAdmin:12}")
    private int perPageAdmin;

    // == bean methods ==
    @Bean
    @PerPage
    public int perPage() {
        return perPage;
    }

    @Bean
    @PerPageAdmin
    public int perPageAdmin() {
        return perPageAdmin;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RequestInterceptor());
    }
}
