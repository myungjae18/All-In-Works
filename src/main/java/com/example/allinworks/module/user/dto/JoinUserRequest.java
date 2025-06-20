package com.example.allinworks.module.user.dto;

import lombok.Data;

@Data
public class JoinUserRequest {
    private String userName;
    private String position;
    private String email;
    private String userPw;
    private String birthday;
    private String contact;
    private String address;
    private String phoneNo;
    private String deptNo;

    private String positionNo;

}
