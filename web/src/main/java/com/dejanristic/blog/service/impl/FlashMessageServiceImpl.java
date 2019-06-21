package com.dejanristic.blog.service.impl;

import com.dejanristic.blog.domain.Message;
import com.dejanristic.blog.service.FlashMessageService;
import com.dejanristic.blog.util.AttributeNames;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Service
public class FlashMessageServiceImpl implements FlashMessageService {

    @Override
    public void flash(
            String type,
            String message,
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute(
                AttributeNames.FLASH_MESSAGE,
                new Message(type, message)
        );
    }

}
