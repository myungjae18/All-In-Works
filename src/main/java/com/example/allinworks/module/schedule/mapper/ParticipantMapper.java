package com.example.allinworks.module.schedule.mapper;

import com.example.allinworks.module.schedule.domain.Participant;
import com.example.allinworks.module.schedule.dto.ParticipantDto;
import com.example.allinworks.module.user.domain.User;

public class ParticipantMapper {
    public static Participant dtoToParticipant(ParticipantDto dto) {
        return Participant.builder()
                //.schedule(ScheduleMapper.eventToSchedule(dto.getEventDto()))
                .user(UserMapper.dtoToUser(dto.getUserDto()))
                .build();
    }

    public static Participant makeParticipant(String userNo) {
        return Participant.builder()
                .user(User.builder().userNo(userNo).build())
                .build();
    }

    public static ParticipantDto participantToDto(Participant participant) {
        ParticipantDto dto = new ParticipantDto();

        dto.setParticipantNo(participant.getParticipantNo());
        dto.setUserDto(UserMapper.userToDto(participant.getUser()));

        return dto;
    }
}
