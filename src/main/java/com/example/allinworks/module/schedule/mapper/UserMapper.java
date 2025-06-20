package com.example.allinworks.module.schedule.mapper;

import com.example.allinworks.module.schedule.dto.UserDto;
import com.example.allinworks.module.user.domain.User;
import com.example.allinworks.module.user.dto.DepartmentDto;

public class UserMapper {
    public static User dtoToUser(UserDto dto) {
        return User.builder()
                .userNo(String.valueOf(dto.getUserNo()))
                .userName(dto.getUserName())
                .position(dto.getPosition())
                .build();
    }

    public static UserDto userToDto(User user) {
        UserDto dto = new UserDto();
        DepartmentDto deptDto = new DepartmentDto();

        deptDto.setDeptNo(user.getDepartment().getDeptNo());
        deptDto.setDeptName(user.getDepartment().getDeptName());

        dto.setUserNo(user.getUserNo());
        dto.setUserName(user.getUserName());
        dto.setPosition(user.getPosition());
        dto.setDepartmentDto(deptDto);

        return dto;
    }
}
