package com.example.allinworks.module.schedule.domain;

import com.example.allinworks.module.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PARTICIPANT")
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PARTICIPANT_NO")
    private int participantNo;

    @ManyToOne
    @JoinColumn(name = "SCHEDULE_NO")
    private Schedule schedule;

    @ManyToOne
    @JoinColumn(name = "USER_NO")
    private User user;
}
