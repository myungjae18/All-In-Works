package com.example.allinworks.module.schedule;

import com.example.allinworks.module.schedule.domain.MeetingRoom;
import com.example.allinworks.module.schedule.domain.Schedule;
import com.example.allinworks.module.schedule.repository.ScheduleRepository;
import com.example.allinworks.module.user.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@Transactional
public class ScheduleRepositoryTest {
    @Autowired
    private ScheduleRepository scheduleRepository;

    //db
    @Test
    void saveSchedule() {
        //테스트용 Schedule entity 생성
        Schedule schedule = Schedule.builder()
                .title("test1")
                .memo("3333")
                .user(User.builder()
                        .userNo("1").build())
                .meetingRoom(MeetingRoom.builder()
                        .name("meeting1").build()).
                build();

        //entity save
        Schedule savedSchedule = scheduleRepository.save(schedule);
        System.out.println("결과는" + savedSchedule.getMemo());
    }
}
