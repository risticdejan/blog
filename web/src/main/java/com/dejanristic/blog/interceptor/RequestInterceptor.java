package com.dejanristic.blog.interceptor;

import com.dejanristic.blog.service.VisitorService;
import com.dejanristic.blog.service.impl.UserDetailsImpl;
import com.dejanristic.blog.util.AttributeNames;
import java.time.LocalDateTime;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class RequestInterceptor implements HandlerInterceptor {

    @Autowired
    private VisitorService vistorService;

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) throws Exception {

        HttpSession session = request.getSession();

        if (session.isNew()) {
            String day = LocalDateTime.now().getDayOfWeek().name();
            this.vistorService.add(day);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            request.setAttribute(
                    AttributeNames.CURRENT_USER,
                    ((UserDetailsImpl) authentication.getPrincipal()).getUser()
            );
        }

        return true;
    }

}
