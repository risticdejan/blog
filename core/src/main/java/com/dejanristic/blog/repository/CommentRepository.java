package com.dejanristic.blog.repository;

import com.dejanristic.blog.domain.Comment;
import org.springframework.data.repository.CrudRepository;

public interface CommentRepository extends CrudRepository<Comment, Long> {

}
