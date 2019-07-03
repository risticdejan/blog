package com.dejanristic.blog.service;

import com.dejanristic.blog.domain.Visitor;
import java.util.List;

public interface VisitorService {

    List<Visitor> getAll();

    void add(String day);

    void create(Visitor vistor);

    int max();

}
