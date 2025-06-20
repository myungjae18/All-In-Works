package com.example.allinworks.module.project.repository;

import com.example.allinworks.module.project.domain.ProjectDetail;
import com.example.allinworks.module.project.domain.ProjectStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProjectDetailRepository extends JpaRepository<ProjectDetail, Integer> {
    long countByStatus(ProjectStatus status);
}
