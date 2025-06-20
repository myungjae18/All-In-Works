package com.example.allinworks.module.project.dto;

import com.example.allinworks.module.project.domain.ProjectDetail;
import com.example.allinworks.module.user.dto.JoinUserRequest;
import com.example.allinworks.module.user.mapper.UserMapper;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ProjectDetailResponse {
    private String id;//detailNo
    private String title;
    private String author;//userName
    private Integer cardOrder; //order
    private LocalDate start;
    private LocalDate end;
    private String status;
}

