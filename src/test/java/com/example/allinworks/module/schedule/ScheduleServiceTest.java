package com.example.allinworks.module.schedule;

import com.example.allinworks.module.schedule.domain.MeetingRoom;
import com.example.allinworks.module.schedule.domain.Schedule;
import com.example.allinworks.module.schedule.repository.ScheduleRepository;
import com.example.allinworks.module.schedule.service.ScheduleService;
import com.example.allinworks.module.user.domain.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

//Mockito 기능 활성화
@ExtendWith(MockitoExtension.class)
public class ScheduleServiceTest {
    @Mock
    private ScheduleRepository scheduleRepository;

    //ScheduleService를 생성하고, scheduleRepository Mock을 자동 주입
    @InjectMocks
    private ScheduleService scheduleService;

//    @Test
//    void registerSchedule() {
//        //테스트용 Schedule entity 생성
//        Schedule schedule = Schedule.builder()
//                .title("test1").memo("3333")
//                .user(User.builder()
//                        .userNo("1").build())
//                .meetingRoom(MeetingRoom.builder()
//                        .name("meeting1").build())
//                .build();
//
//        //repository.save()가 어떤 schedule를 받든, 항상 위에서 만든 schedule 객체를 반환하도록 지정
//        //실제 DB에 저장하지 않고, 원하는 결과를 직접 지정
//        when(scheduleRepository.save(schedule)).thenReturn(schedule);
//
//        //테스트 대상인 scheduleService의 register 메서드를 실제로 호출
//        Schedule savedSchedule = scheduleService.register(schedule);
//        //결과로 받은 schedule 값 검증
//        assertThat(savedSchedule.getTitle()).isEqualTo("test1");
//    }

    @Test
    void getSchedule() {
        System.out.println(scheduleRepository.findAll());
    }
}
