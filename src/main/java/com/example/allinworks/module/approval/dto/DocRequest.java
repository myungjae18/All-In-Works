package com.example.allinworks.module.approval.dto;

import lombok.Data;

import java.util.List;

@Data
public class DocRequest {
    private String title;
    private String content;
    private String userId;
    private String approveUserId;
    private String confirmUserId;
//    private List<ApproverDto> approveUserList;
}
