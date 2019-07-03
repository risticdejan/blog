package com.dejanristic.blog.domain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JsonRespone {

    private final String status;
    private final Object body;

    public JsonRespone(String status, Object body) {
        this.status = status;
        this.body = body;
    }
}
