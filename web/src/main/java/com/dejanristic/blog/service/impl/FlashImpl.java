package com.dejanristic.blog.service.impl;

import com.dejanristic.blog.domain.model.Message;
import com.dejanristic.blog.service.Flash;
import com.dejanristic.blog.util.AttributeNames;
import com.dejanristic.blog.util.FlashNames;
import java.util.HashMap;
import java.util.Map;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component("flash")
@Scope(value = "session", proxyMode = ScopedProxyMode.INTERFACES)
public class FlashImpl implements Flash {

    Map<String, Message> message = new HashMap();

    private void add(Message message) {
        this.message.put(AttributeNames.FLASH_MESSAGE, message);
    }

    @Override
    public void info(String message) {
        this.add(new Message(FlashNames.INFO_TYPE, message));
    }

    @Override
    public void error(String message) {
        this.add(new Message(FlashNames.ERROR_TYPE, message));
    }

    @Override
    public void success(String message) {
        this.add(new Message(FlashNames.SUCCESS_TYPE, message));
    }

    @Override
    public void reset() {
        this.message.clear();
    }

    @Override
    public Map<String, Message> getMessage() {
        return this.message;
    }

}
