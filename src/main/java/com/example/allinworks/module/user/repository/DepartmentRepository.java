package com.example.allinworks.module.user.repository;

import com.example.allinworks.module.user.domain.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, String> {
    Department findByDeptNo(String deptNo);
}
