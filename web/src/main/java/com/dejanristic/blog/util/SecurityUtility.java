package com.dejanristic.blog.util;

import java.security.SecureRandom;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtility {

    private static final String SALT = "salt.dejanristic";

    @Bean
    public static BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12, new SecureRandom(SALT.getBytes()));
    }

    public static int cleanPageParam(String page) {
        int cleanPage;
        try {
            page = (page == null) ? "1" : page;
            cleanPage = Integer.parseInt(page) - 1;
        } catch (NumberFormatException ex) {
            cleanPage = 0;
        }

        return cleanPage;
    }

    public static Long cleanIdParam(String id) {
        Long cleanId;
        try {
            id = (id == null) ? "1" : id;
            cleanId = Long.parseLong(id);
        } catch (NumberFormatException ex) {
            cleanId = 0L;
        }

        return cleanId;
    }
}
