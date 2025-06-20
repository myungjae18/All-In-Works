package com.example.allinworks.module.board.dto;

import lombok.*;

import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CommentDTO {
    private Integer commentNo;
    private String postNo;
    private String userNo;
    private String content;
    private LocalDate createdAt;
    private LocalDate updatedAt;

    private String userName; // DB에 없음 사용자 이름을 표시용
}