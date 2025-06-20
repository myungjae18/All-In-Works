package com.example.allinworks.module.schedule.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParticipantDto {
    private int participantNo;
    private EventResponse eventDto;
    private UserDto userDto;
}
