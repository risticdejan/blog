package com.dejanristic.blog.service.impl;

import com.dejanristic.blog.domain.Comment;
import com.dejanristic.blog.repository.CommentRepository;
import com.dejanristic.blog.service.CommentService;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public List<Comment> findAll() {
        return (List<Comment>) commentRepository.findAll();
    }

    @Override
    public Page<Comment> findAll(Pageable pageable) {
        return commentRepository.findAll(pageable);
    }

    @Override
    public Comment findById(Long id) {
        return commentRepository.findById(id).orElse(null);
    }

    @Override
    public List<Comment> findByArticleId(Long articleId) {
        return commentRepository.findByArticleIdOrderByCreatedAtDesc(articleId);
    }

    @Override
    public Page<Comment> findAllCommentsByUser(Long userId, Pageable pageable) {
        return (Page<Comment>) commentRepository.findByUserId(userId, pageable);
    }

    @Override
    public void delete(Comment commnet) {
        commentRepository.delete(commnet);
    }

    @Override
    public Comment create(Comment comment) {
        comment.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        comment.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
        return commentRepository.save(comment);
    }

}
