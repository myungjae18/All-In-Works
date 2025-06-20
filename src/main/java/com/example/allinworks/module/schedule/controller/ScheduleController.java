package com.example.allinworks.module.schedule.controller;

import com.example.allinworks.module.schedule.dto.*;
import com.example.allinworks.module.schedule.mapper.ScheduleMapper;
import com.example.allinworks.module.schedule.service.ScheduleService;
import com.example.allinworks.module.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.*;
import java.util.List;

@Controller
@AllArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;
    private final UserService userService;

    @GetMapping("/schedule/calendar")
    public String calendar(HttpSession session, Model model) {
        if(session.getAttribute("user") == null) {
            model.addAttribute("message", "Not logged in");
        }
        return "schedule/calendar";
    }

    @GetMapping("/schedule/meeting-room")
    public String meetingRoom(Model model) {

        return "schedule/meeting-room";
    }

    //로그인한 유저 부서 번호를 이용한 스케줄 조회 요청 처리
    //fullCalendar에서 원하는 json 데이터 형태로 전달 필요
    @GetMapping("/schedules/{userNo}")
    @ResponseBody
    public List<EventResponse> getSchedules(
            @RequestParam("start") String start, @RequestParam("end") String end, @PathVariable String userNo) {
        //날짜 형식 변환
        LocalDateTime startAt = ScheduleMapper.convertTimezone(start);
        LocalDateTime endAt = ScheduleMapper.convertTimezone(end);

        System.out.println("스케줄 조회 시 시작일" + startAt);

        return scheduleService.getAllEventsByUserNo(userNo, startAt, endAt);
    }

    //스케줄 등록 요청 처리
    @PostMapping("/schedules")
    public ResponseEntity<?> addSchedule(@RequestBody EventRequest event) {
        System.out.println("이벤트 등록 요청 시 제목: " + event.getTitle());
        System.out.println("이벤트 등록 요청 시 참여자 리스트 길이" + event.getUserNoList().size());
        scheduleService.register(event);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    //스케줄 수정 요청 처리
    @PutMapping("/schedules/{eventId}")
    @ResponseBody
    public ResponseEntity<?> updateSchedule(@PathVariable("eventId") String eventId,
                                           @RequestBody EventRequest event) {
        scheduleService.updateSchedule(eventId, event);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //스케줄 삭제 요청 처리
    @DeleteMapping("/schedules/{eventId}")
    @ResponseBody
    public ResponseEntity<?> deleteSchedule(@PathVariable("eventId") String eventId) {
        if(scheduleService.deleteSchedule(Integer.parseInt(eventId))) {//삭제 실패
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {//삭제 성공
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    //사내 모든 유저 조회 요청 처리
    @GetMapping("/users")
    @ResponseBody
    public List<UserDto> getUsers() {
        return userService.getColleagues();
    }

    //요청한 기간에 사용 가능한 회의실 조회
    @GetMapping("/meeting-rooms/available")
    public List<AvailableRoom> getMeetingRooms(@RequestParam String start, @RequestParam String end) {
        LocalDateTime startAt = ScheduleMapper.convertTimezone(start);
        LocalDateTime endAt = ScheduleMapper.convertTimezone(end);

        return scheduleService.getAvailableRooms(startAt, endAt);
    }
}
