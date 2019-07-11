package com.dejanristic.blog.interceptor;

import com.dejanristic.blog.domain.model.Message;
import com.dejanristic.blog.service.Flash;
import com.dejanristic.blog.util.AttributeNames;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class FlashInterceptor implements HandlerInterceptor {

    @Autowired
    private Flash flash;

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) throws Exception {
        Map<String, Message> message = flash.getMessages();

        if (!message.isEmpty()) {
            request.setAttribute(
                    AttributeNames.FLASH_MESSAGE,
                    message.get(AttributeNames.FLASH_MESSAGE)
            );
            flash.reset();
        }

        return true;
    }

}
