package com.example.allinworks.module.schedule.service;

import com.example.allinworks.module.schedule.domain.MeetingRoom;
import com.example.allinworks.module.schedule.domain.Participant;
import com.example.allinworks.module.schedule.domain.Schedule;
import com.example.allinworks.module.schedule.dto.*;
import com.example.allinworks.module.schedule.mapper.ScheduleMapper;
import com.example.allinworks.module.schedule.repository.MeetingRoomRepository;
import com.example.allinworks.module.schedule.repository.ParticipantRepository;
import com.example.allinworks.module.schedule.repository.ScheduleRepository;
import com.example.allinworks.module.user.domain.User;
import com.example.allinworks.module.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final ParticipantRepository participantRepository;
    private final UserRepository userRepository;
    private MeetingRoomRepository meetingRoomRepository;
    private SimpMessagingTemplate messagingTemplate;

    @Transactional
    //일정 등록
    public void register(EventRequest event) {
        //일정 등록 후 해당 일정의 참여된 유저 모두에게 브로드캐스팅
        scheduleRepository.save(ScheduleMapper.requestToSchedule(event));
        event.getUserNoList().forEach(userNo -> {
            messagingTemplate.convertAndSend("/topic/schedule/" + userNo, "updated");
        });
    }

    //해당 기간의 모든 일정을 가져와서 CalendarEventDto로 변환 후 반환
    public List<EventResponse> getAllEvents(ZonedDateTime start, ZonedDateTime end) {
        List<Schedule> schedules = scheduleRepository.findAllByStartAtBetween(start, end);
        return schedules.stream().map(ScheduleMapper::scheduleToEvent).collect(Collectors.toList());
    }

    //로그인한 유저의 유저번호를 통해 참여한(participant 이용) 해당 기간 모든 일정을 가져와 EventResponse 배열로 변환 후 반환
    public List<EventResponse> getAllEventsByUserNo(String userNo, LocalDateTime start, LocalDateTime end) {
        List<Schedule> schedules =
                scheduleRepository.findSchedulesByUserNoAndPeriod(userNo, start, end);

        return schedules.stream().map(ScheduleMapper::scheduleToEvent).collect(Collectors.toList());
    }

    //일정 수정
    @Transactional
    public void updateSchedule(String eventNo, EventRequest event) {
        // 1) 영속성 컨텍스트에서 기존 Schedule 로드
        Schedule schedule = scheduleRepository.findById(
                        Integer.parseInt(eventNo))
                .orElseThrow(() -> new IllegalArgumentException("Schedule not found: " + eventNo));

        // 2) 단순 필드 업데이트
        schedule.setTitle(event.getTitle());
        //부서원이 일정을 공유하므로 수정 시 작성자 변경
        Optional<User> author = userRepository.findById(event.getAuthor());
        author.ifPresent(schedule::setUser);
        schedule.setStartAt(ScheduleMapper.convertTimezone(event.getStart()));
        schedule.setEndAt(ScheduleMapper.convertTimezone(event.getEnd()));
        schedule.setMemo(event.getMemo());
        schedule.setType(event.getType());

        // 3) Participants 양방향 관계 동기화
        //    기존 참여자 모두 삭제
        schedule.getParticipants().clear();
        //    새 dto 참여자 목록으로 다시 추가
        event.getUserNoList().forEach(userNo -> {
            Participant participant = Participant.builder()
                    .user(
                            User.builder()
                                    .userNo(userNo)
                                    .build()
                    )
                    .build();
            schedule.addParticipant(participant);
        });

        List<Participant> participants = schedule.getParticipants();

        // 4) save 호출 (또는 생략해도 dirty-checking 으로 반영)
        scheduleRepository.save(schedule);
        // 5) 일정 변경되었음을 일정에 참여된 모든 유저에게 브로드캐스팅

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit() {
                // 커밋 후 브로드캐스트
                participants.forEach(participant -> {
                    messagingTemplate.convertAndSend("/topic/schedule/" + participant.getUser().getUserNo(), "updated");
                });
            }
        });
    }


    //일정 삭제
    public boolean deleteSchedule(Integer eventNo) {
        //해당 일정 참여자 조회
        List<Participant> participants = participantRepository.findAllBySchedule_ScheduleNo(eventNo);

        //삭제 후 일정에 참여했던 유저들에게 브로드캐스팅
        scheduleRepository.deleteById(eventNo);
        participants.forEach(participant -> {
            messagingTemplate.convertAndSend("/topic/schedule/" + participant.getUser().getUserNo(), "updated");
        });

        return scheduleRepository.existsById(eventNo);
    }

    //요청한 시각에 사용 가능한 회의실 조회
    public List<AvailableRoom> getAvailableRooms(LocalDateTime start, LocalDateTime end) {
        List<MeetingRoom> availableRooms = meetingRoomRepository.findAvailableRooms(start, end);

        return null;
    }
}
