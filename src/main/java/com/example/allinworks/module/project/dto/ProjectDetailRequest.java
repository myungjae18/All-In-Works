package com.example.allinworks.module.project.dto;

import com.example.allinworks.module.project.domain.ProjectStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ProjectDetailRequest {
    private String title;
    private String part;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    private Integer detailOrder;
    private ProjectStatus status;
    private Integer userNo;
    private Integer projectNo;
}
