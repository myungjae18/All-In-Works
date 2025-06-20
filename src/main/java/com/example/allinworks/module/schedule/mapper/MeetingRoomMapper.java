package com.example.allinworks.module.schedule.mapper;

import com.example.allinworks.module.schedule.domain.MeetingRoom;
import com.example.allinworks.module.schedule.dto.EventResponse;
import com.example.allinworks.module.schedule.dto.AvailableRoom;

import java.util.List;

public class MeetingRoomMapper {
//    public static MeetingRoom dtoToMeetingRoom(MeetingRoomResponse dto) {
//        return MeetingRoom.builder()
//                .name(dto.getName())
//                .capacity(dto.getCapacity())
//                .isAvailable(dto.getIsAvailable())
//                .build();
//    }

    public static AvailableRoom meetingRoomToResponse(MeetingRoom room) {
        AvailableRoom dto = new AvailableRoom();

        dto.setMeetingRoomNo(room.getMeetingRoomNo());
        dto.setName(room.getName());
        dto.setCapacity(room.getCapacity());

//        List<EventResponse> dtoList = room.getSchedules().stream()
//                .map(ScheduleMapper::scheduleToEvent)
//                .toList();

        return dto;
    }
}
