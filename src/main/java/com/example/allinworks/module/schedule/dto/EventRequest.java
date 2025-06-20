package com.example.allinworks.module.schedule.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

//일정 등록 및 수정 시 사용하는 DTO
@Getter
@Setter
public class EventRequest {
    private String title;
    private String author;
    private String start;
    private String end;
    private String memo;
    private String type;
    private List<String> userNoList = new ArrayList<>();
}
