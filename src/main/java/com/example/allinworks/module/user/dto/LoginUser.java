package com.example.allinworks.module.user.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Data @ToString
public class LoginUser {
    private String userNo;
    private String userName;
    private String email;
    private String userPw;
    private String birthday;
    private String contact;
    private String address;
    private String position;
    private LocalDate hireDate;
    private DepartmentDto department;
}
