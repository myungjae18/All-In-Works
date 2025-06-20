package com.example.allinworks.module.schedule.repository;

import com.example.allinworks.module.schedule.domain.Schedule;
import com.example.allinworks.module.user.domain.Department;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {
    List<Schedule> findAllByStartAtBetween(ZonedDateTime startAtAfter, ZonedDateTime startAtBefore);


    List<Schedule> findAllByUser_Department_DeptNoAndStartAtBetween(
            String user_department_deptNo, LocalDateTime startAt, LocalDateTime startAtBefore);

    @Query(value = """
    SELECT s.*
    FROM PARTICIPANT p
    JOIN SCHEDULE s ON p.SCHEDULE_NO = s.SCHEDULE_NO
    WHERE p.USER_NO = :userNo
      AND s.START_AT >= :start
      AND s.END_AT <= :end
""", nativeQuery = true)
    List<Schedule> findSchedulesByUserNoAndPeriod(
            @Param("userNo") String userNo,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}
