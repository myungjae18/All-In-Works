package com.example.allinworks.module.user.mapper;

import com.example.allinworks.module.user.domain.User;
import com.example.allinworks.module.user.dto.JoinUserRequest;

public class UserMapper {
    public static User dtoToUser(JoinUserRequest dto) {
        return User.builder()
                .userName(dto.getUserName())
                .email(dto.getEmail())
                .userPw(dto.getUserPw())
                .birthday(dto.getBirthday())
                .contact(dto.getContact())
                .address(dto.getAddress())
                //.hireDate(dto.getHireDate())
                .position(dto.getPosition())
                //.department(dto.getDept())
            //    .phoneNo(dto.getPhoneNo())
                .build();
    }

    public static JoinUserRequest userToDto(User user) {
        JoinUserRequest dto = new JoinUserRequest();

        //dto.setUserNo(user.getUserNo());
        dto.setUserName(user.getUserName());
        dto.setEmail(user.getEmail());
        dto.setUserPw(user.getUserPw());
        dto.setBirthday(user.getBirthday());
        dto.setContact(user.getContact());
        dto.setAddress(user.getAddress());
        //dto.setHireDate(user.getHireDate());
        dto.setPosition(user.getPosition());
        //dto.setDept(user.getDepartment());
      //  dto.setPhoneNo(user.getPhoneNo());

        return dto;
    }
}
