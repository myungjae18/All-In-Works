package com.example.allinworks.module.schedule.domain;

import com.example.allinworks.module.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "SCHEDULE")
@Entity
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SCHEDULE_NO")
    private Integer scheduleNo;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "START_AT")
    private LocalDateTime startAt;

    @Column(name = "END_AT")
    private LocalDateTime endAt;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "MEMO")
    private String memo;

    @Column(name = "TYPE")
    private String type;

    @ManyToOne
    @JoinColumn(name = "USER_NO")
    private User user;

    @ManyToOne
    @JoinColumn(name = "MEETINGROOM_NO")
    private MeetingRoom meetingRoom;

    @Builder.Default //builder 생성 시 초기화하지 않고 해당 기본값 사용
    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Participant> participants = new ArrayList<>();

    //양방향 연관관계를 일관되게 동기화
    public void addParticipant(Participant participant) {
        participant.setSchedule(this);
        participants.add(participant);
    }
}
