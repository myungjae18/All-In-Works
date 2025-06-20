package com.example.allinworks.module.user.repository;

import com.example.allinworks.module.user.domain.Department;
import com.example.allinworks.module.user.domain.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmailAndUserPw(String email, String userPw);

    Optional<User> findByEmail(String email);
  
    List<User> findByDepartment_DeptNo(String departmentDeptNo);
  
    //departmentNo로 user 조회
    @Query(value = "SELECT * FROM USER_INFO WHERE DEPT_NO = :departmentNo", nativeQuery = true)
    List<User> findByDepartmentNoNative(@Param("departmentNo")String departmentNo);

    List<User> findByUserNameContaining(String keyword);


    @Query("SELECT DISTINCT u.department FROM User u WHERE u.department IS NOT NULL")
    List<Department> findDistinctDepartments();

    Collection<User> findByUserNoIn(List<String> userNos);

    /*@Query("SELECT DISTINCT u.position FROM User u WHERE u.position IS NOT NULL")
    List<Position> findDistinctPositions();*/

    @Query("SELECT DISTINCT p.positionNo FROM Position p")
    List<String> findDistinctPositionNames();

    List<User> findAllByOrderByDepartment_DeptNo();

    @EntityGraph(attributePaths = {"department"})
    Optional<User> findWithDepartmentByUserNo(String userNo);

    // 조인 페치 추가
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.department WHERE u.userName LIKE %:keyword%")
    List<User> searchWithDepartmentByUserNameContaining(@Param("keyword") String keyword);
}

