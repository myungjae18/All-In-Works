package com.example.allinworks.module.schedule.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Getter
@Setter
//fullCalendar에서 요구하는 일정의 형태
public class EventResponse {
    private Integer id;
    private String title;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private OffsetDateTime start;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private OffsetDateTime end;

    //fullCalendar에서 기본 지원하지 않는 데이터
    private EventExtendsProps extendedProps;
}
