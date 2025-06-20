package com.example.allinworks.module.user.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class AttendanceDto {
    private Integer attendanceNo;
    private LocalDate date;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private JoinUserRequest user;
}
