package com.example.allinworks.module.approval.dto;

import lombok.Data;

@Data
public class DocResponse {
    private Integer id;
    private String userId;
    private String userName;
    private String title;
    private String content;
    private String status;
    private String createDate;
}
