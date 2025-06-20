package com.example.allinworks.module.board.repository;

import com.example.allinworks.module.board.domain.Comment;
import com.example.allinworks.module.board.domain.Post;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findAllByPost(Post post);
}