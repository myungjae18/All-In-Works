package com.example.allinworks.module.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
//kanban board의 column(To Do, In Progress..)
public class KanbanColumn {
    private String id;
    private String title;
    @JsonProperty("class")
    private String className;
    private List<ResponseCard> item;
}
