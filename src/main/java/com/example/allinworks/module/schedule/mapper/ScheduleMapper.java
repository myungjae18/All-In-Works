package com.example.allinworks.module.schedule.mapper;

import com.example.allinworks.module.schedule.domain.Participant;
import com.example.allinworks.module.schedule.domain.Schedule;
import com.example.allinworks.module.schedule.dto.*;
import com.example.allinworks.module.user.domain.User;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

public class ScheduleMapper {
    //timezone 세팅
    public static LocalDateTime convertTimezone(String timezone) {
        Instant instant = Instant.parse(timezone);
        return LocalDateTime.ofInstant(instant, ZoneId.of("Asia/Seoul"));
    }

    //요청 DTO -> entity
    public static Schedule requestToSchedule(EventRequest event) {
        User author = User.builder().userNo(event.getAuthor()).build();//작성자

        Schedule schedule = Schedule.builder()
                .title(event.getTitle())
                .user(author)
                .createdAt(LocalDateTime.now())
                .startAt(convertTimezone(event.getStart()))
                .endAt(convertTimezone(event.getEnd()))
                .memo(event.getMemo())
                .type(event.getType())
                .build();

        for(String userNo : event.getUserNoList()) {
            User user = User.builder().userNo(userNo).build();
            schedule.addParticipant(Participant.builder().user(user).build());
        }

        return schedule;
    }

    //entity -> 응답 DTO
    public static EventResponse scheduleToEvent(Schedule schedule) {
        EventResponse eventDto = new EventResponse();

        eventDto.setId(schedule.getScheduleNo());
        eventDto.setTitle(schedule.getTitle());
        eventDto.setStart(
                schedule.getStartAt()
                        .atZone(ZoneId.of("Asia/Seoul")).toOffsetDateTime()
        );
        eventDto.setEnd(
                schedule.getEndAt()
                        .atZone(ZoneId.of("Asia/Seoul")).toOffsetDateTime()
        );

        EventExtendsProps props = new EventExtendsProps();

        props.setMemo(schedule.getMemo());
        props.setUserDto(UserMapper.userToDto(schedule.getUser()));
        props.setType(schedule.getType());

        List<ParticipantDto> dtoList = schedule.getParticipants().stream()
                .map(ParticipantMapper::participantToDto)
                .toList();

        props.setParticipants(dtoList);
        eventDto.setExtendedProps(props);

        return eventDto;
    }
}
