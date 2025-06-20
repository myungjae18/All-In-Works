package com.example.allinworks.module.schedule.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

//추가 데이터
@Getter
@Setter
public class EventExtendsProps {
    private String memo;
    private LocalDateTime createdAt;

    //일정 생성자
    private UserDto userDto;
    //일정 참여자 명단
    private List<ParticipantDto> participants;
    //사용 가능한 회의실 목록
    private List<AvailableRoom> rooms;
    //일정 구분
    private String type;
}
