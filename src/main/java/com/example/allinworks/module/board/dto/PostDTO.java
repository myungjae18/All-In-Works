package com.example.allinworks.module.board.dto;

import com.example.allinworks.module.user.domain.User;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.allinworks.module.schedule.dto.ParticipantDto;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PostDTO {
    private Integer postNo;
    private Integer boardNo;
    private String userNo;
    private String fileNo;
    private String title;
    private String content;
    private int views;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean notice;
    private List<CommentDTO> comments =  new ArrayList<>();

    private String userName; // userNo -> userName으로 변환해서 저장할 변수
}