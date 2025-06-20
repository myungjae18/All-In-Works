package com.example.allinworks.module.approval.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DocCreate {
    private String title;
    private String date;
    private String userDept;
    private String userName;
    private String userNo;
}
