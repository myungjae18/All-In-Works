package com.example.allinworks.module.project.repository;

import com.example.allinworks.module.project.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Integer> {
    Optional<Project> findByDepartment_DeptNo(String deptNo);
}
