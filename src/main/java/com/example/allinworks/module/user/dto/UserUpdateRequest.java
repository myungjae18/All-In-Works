package com.example.allinworks.module.user.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
//
public class UserUpdateRequest {
    private String userName;
    private String email;
    private String birthday;
    private String address;
}
