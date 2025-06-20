package com.example.allinworks.module.schedule.repository;

import com.example.allinworks.module.schedule.domain.MeetingRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MeetingRoomRepository extends JpaRepository<MeetingRoom, Integer> {
    //해당 시간 내에 사용 가능한 회의실 목록 반환
    @Query("""
                Select m FROM MeetingRoom m WHERE NOT EXISTS (
                    SELECT s FROM Schedule s 
                        WHERE s.meetingRoom = m
                            AND s.startAt < :end
                            AND s.endAt > :start
                    )
            """)
    List<MeetingRoom> findAvailableRooms(
            @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
