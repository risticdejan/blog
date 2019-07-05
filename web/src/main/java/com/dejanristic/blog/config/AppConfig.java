package com.dejanristic.blog.config;

import com.dejanristic.blog.annotation.PerPage;
import com.dejanristic.blog.annotation.PerPageAdmin;
import com.dejanristic.blog.interceptor.FlashInterceptor;
import com.dejanristic.blog.interceptor.RequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
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

    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(100000); //1MB
        return multipartResolver;
    }

    @Autowired
    private RequestInterceptor requestInterceptor;

    @Autowired
    private FlashInterceptor flashInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestInterceptor);
        registry.addInterceptor(flashInterceptor);
    }
}
