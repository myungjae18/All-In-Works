package com.example.allinworks.module.board.repository;

import com.example.allinworks.module.board.domain.Board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> {
    Board findByDeptNo(String deptNo);

    
}