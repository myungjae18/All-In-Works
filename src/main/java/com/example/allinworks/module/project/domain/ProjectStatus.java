package com.example.allinworks.module.project.domain;

import lombok.Getter;

//kanban board의 column 역할을 수행
@Getter
public enum ProjectStatus {
    TODO("To Do"),
    IN_PROGRESS("In Progress"),
    IN_REVIEW("In Review"),
    DONE("Done");

    private final String label;

    ProjectStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    // label로 enum 찾기 (순회 방식)
    public static ProjectStatus fromLabel(String label) {
        for (ProjectStatus status : values()) {
            if (status.label.equals(label)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown label: " + label);
    }
}
