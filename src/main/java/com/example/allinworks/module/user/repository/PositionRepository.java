package com.example.allinworks.module.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.allinworks.module.user.domain.Position;

import java.util.Collection;
import java.util.List;

//
public interface PositionRepository extends JpaRepository<Position, String> {
    Position findByPositionNo(String positionNo);

    List<Position> findAllByPositionNoIn(Collection<String> positionNos);
}
