package com.dejanristic.blog.service;

import com.dejanristic.blog.domain.Message;
import java.util.Map;

public interface Flash {

    void info(String message);

    void success(String message);

    void error(String message);

    void reset();

    Map<String, Message> getMessage();
}
