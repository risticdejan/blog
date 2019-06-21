package com.dejanristic.blog.service;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public interface FlashMessageService {

    public void flash(
            String type,
            String message,
            RedirectAttributes redirectAttributes
    );
}
