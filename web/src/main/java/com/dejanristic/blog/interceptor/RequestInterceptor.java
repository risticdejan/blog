package com.dejanristic.blog.interceptor;

import com.dejanristic.blog.service.impl.UserDetailsImpl;
import com.dejanristic.blog.util.AttributeNames;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class RequestInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) throws Exception {
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
