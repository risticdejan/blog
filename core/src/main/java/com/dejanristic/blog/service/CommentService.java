package com.dejanristic.blog.service;

import com.dejanristic.blog.domain.Comment;
import java.util.List;

public interface CommentService {

    List<Comment> findAll();

    Comment findById(Long id);

    List<Comment> findByArticleId(Long articleId);

    void delete(Comment commnet);

    Comment create(Comment commnet);
}
