package com.example.allinworks.module.board.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "BOARD")
@Getter @ToString
@AllArgsConstructor
@NoArgsConstructor
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOARD_NO", nullable = false)
    private Integer boardNo;

    @Column(name = "DEPT_NO", nullable = false)
    private String deptNo;

    @Column(name = "NAME", nullable = false, length = 100)
    private String name;
}