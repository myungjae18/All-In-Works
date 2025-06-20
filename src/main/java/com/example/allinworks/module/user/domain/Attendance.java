package com.example.allinworks.module.user.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter @Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ATTENDANCE")
public class Attendance {

    @Id
    @Column(name = "ATTENDANCE_NO", nullable = false, length = 10)
    private String attendanceNo;

    @ManyToOne
    @JoinColumn(name = "USER_NO", nullable = false)
    private User user;

    @Column(name = "DATE", nullable = false)
    private LocalDate date;

    @Column(name = "CHECK_IN")
    private LocalTime checkIn;

    @Column(name = "CHECK_OUT")
    private LocalTime checkOut;
}