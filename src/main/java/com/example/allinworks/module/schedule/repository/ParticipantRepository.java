package com.example.allinworks.module.schedule.repository;

import com.example.allinworks.module.schedule.domain.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Integer> {
    List<Participant> findAllBySchedule_ScheduleNo(Integer eventNo);
}
