package com.dejanristic.blog.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Message {

    private String type;
    private String text;

    public Message(String type, String text) {
        this.type = type;
        this.text = text;
    }
}
