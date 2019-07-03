package com.dejanristic.blog.repository;

import com.dejanristic.blog.domain.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface VisitorRepository extends JpaRepository<Visitor, Long> {

    Visitor findByDay(String day);

    @Query(value = "SELECT max(v.count) FROM Visitor v")
    int max();
}
