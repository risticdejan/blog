package com.dejanristic.blog.service.impl;

import com.dejanristic.blog.domain.Visitor;
import com.dejanristic.blog.repository.VisitorRepository;
import com.dejanristic.blog.service.VisitorService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class VisitorServiceImpl implements VisitorService {

    private VisitorRepository visitorRepository;

    @Autowired
    public VisitorServiceImpl(VisitorRepository visitorRepository) {
        this.visitorRepository = visitorRepository;
    }

    @Override
    public List<Visitor> getAll() {
        return this.visitorRepository.findAll();
    }

    @Override
    public void add(String day) {
        Visitor vistor = this.visitorRepository.findByDay(day);
        vistor.setCount(vistor.getCount() + 1);
        this.visitorRepository.save(vistor);
    }

    @Override
    public void create(Visitor visitor) {
        Visitor oldVisitor = this.visitorRepository.findByDay(visitor.getDay());

        if (oldVisitor != null) {
            log.info("vistor {} already exists. Nothing will be done.", visitor.getDay());
        } else {
            visitor.setDay(visitor.getDay().toUpperCase());
            this.visitorRepository.save(visitor);
        }
    }

    @Override
    public int max() {
        return this.visitorRepository.max();
    }

}
