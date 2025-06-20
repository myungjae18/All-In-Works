package com.example.allinworks.module.schedule.dto;

import com.example.allinworks.module.user.dto.DepartmentDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserDto {
    private String userNo;
    private String userName;
    private String position;
    private String department;

    private DepartmentDto departmentDto;
}
