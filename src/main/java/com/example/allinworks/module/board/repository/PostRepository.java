package com.example.allinworks.module.board.repository;

import com.example.allinworks.module.board.domain.Post;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    public List<Post>findAllByBoardNo(int boardNo);

}