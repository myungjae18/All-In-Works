package com.example.allinworks.module.approval.dto;

import lombok.Data;

@Data
public class DocDetail {
    private Integer id;
    private String userName;
    private String title;
    private String content;
    private String userId;
    private String createDate;
    private String dept;
    private String position;
    private int approvalNo;
    private int confirmNo;
    private String approveUserNo;
    private String confirmUserNo;
    private String approveUserName;
    private String confirmUserName;
    private String approveDate;
    private String confirmDate;
    private String approveStatus;
    private String confirmStatus;
    private String approveUserPosition;
    private String confirmUserPosition;

}
