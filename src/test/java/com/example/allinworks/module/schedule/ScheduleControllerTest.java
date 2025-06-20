package com.example.allinworks.module.schedule;

import com.example.allinworks.module.schedule.controller.ScheduleController;
import com.example.allinworks.module.schedule.domain.MeetingRoom;
import com.example.allinworks.module.schedule.domain.Schedule;
import com.example.allinworks.module.schedule.service.ScheduleService;
import com.example.allinworks.module.user.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ScheduleController.class)
public class ScheduleControllerTest {
    @Autowired
    MockMvc mvc;//가짜 HTTP 요청을 보낼 수 있는 객체

    @MockitoBean
    ScheduleService scheduleService;

//    @Test
//    void registerSchedule() throws Exception {
//        //테스트용 eventDto 생성
//        Schedule schedule = Schedule.builder()
//                .title("test1").memo("3333")
//                .user(User.builder()
//                        .userNo("1").build())
//                .meetingRoom(MeetingRoom.builder()
//                        .name("meeting1").build())
//                .build();
//        when(scheduleService.register(any(Schedule.class))).thenReturn(schedule);
//
//        //HTTP Post /schedules 요청을 보냄
//        mvc.perform(post("/schedules")
//                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                .param("title", "test1")//폼 필드 이름과 값
//                .param("memo", "3333"))
//                .andExpect(status().isOk())//http status 확인
//                .andExpect(view().name("/schedule/calendar"))//반환된 view 이름이 맞는지
//                .andExpect(model().attributeExists("schedule"))//model에 schedule이란 이름을 가진 객체가 존재하는지
//                //title 검증
//                .andExpect(model().attribute("schedule", hasProperty("title", is("test1"))));
//    }
}
