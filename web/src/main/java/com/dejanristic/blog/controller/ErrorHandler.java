package com.dejanristic.blog.controller;

import com.dejanristic.blog.util.ViewNames;
import org.springframework.dao.DataAccessException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(DataAccessException.class)
    public String handleDatabaseException(DataAccessException ex) {
        ex.printStackTrace();
        return ViewNames.ERROR;
    }

    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessException(AccessDeniedException ex) {
        return ViewNames.ERROR_403;
    }
}
