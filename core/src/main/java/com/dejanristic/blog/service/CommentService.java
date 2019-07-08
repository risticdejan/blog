package com.dejanristic.blog.service;

import com.dejanristic.blog.domain.Comment;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService {

    List<Comment> findAll();

    Comment findById(Long id);

    List<Comment> findByArticleId(Long articleId);

    Page<Comment> findAll(Pageable pageable);

    Page<Comment> findAllCommentsByUser(Long userId, Pageable pageable);

    void delete(Comment commnet);

    Comment create(Comment commnet);
}
