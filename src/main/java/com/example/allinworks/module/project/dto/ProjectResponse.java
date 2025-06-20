package com.example.allinworks.module.project.dto;

import com.example.allinworks.module.user.dto.DepartmentDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

//jKanban에서 요구하는 board의 형태
@Getter
@Setter
@ToString
public class ProjectResponse {
   private int projectNo;
   private List<KanbanColumn> columns;
}

