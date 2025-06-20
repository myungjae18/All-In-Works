package com.example.allinworks.module.project.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ProjectRequest {
    private int projectNo;
    private List<KanbanColumn> columns;
}
