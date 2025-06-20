package com.example.allinworks.module.user.service;


import com.example.allinworks.module.approval.service.ApprovalService;
import com.example.allinworks.module.schedule.dto.UserDto;
import com.example.allinworks.module.schedule.mapper.UserMapper;
import com.example.allinworks.module.user.domain.Department;
import com.example.allinworks.module.user.domain.User;
import com.example.allinworks.module.user.dto.JoinUserRequest;
import com.example.allinworks.module.user.dto.LoginUser;
import com.example.allinworks.module.user.repository.DepartmentRepository;
import com.example.allinworks.module.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.allinworks.module.user.domain.Position;
import com.example.allinworks.module.user.repository.PositionRepository;

import com.example.allinworks.module.user.dto.UserUpdateRequest;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    @Autowired
    private PositionRepository positionRepository;
//    private final DeptRepository deptRepository;
    private final ApprovalService approvalService;

    @Transactional
    public String joinUser(JoinUserRequest request) {
        log.info("service 시작");
        User user = User.builder()
                .userNo(UUID.randomUUID().toString().substring(0, 6))
                //.deptNo("0123")
                .userName(request.getUserName())
                //.position(request.getPosition())
                //.hireDate(LocalDate.now())
                // 추가된 부분 ⬇️
                .email(request.getEmail())
                .userPw(request.getUserPw())
                .birthday(request.getBirthday())
                //.contact(request.getContact())
                .address(request.getAddress())
                .department(Department.builder().deptNo(request.getDeptNo()).build())
                .position(request.getPositionNo())
                //.position(Position.builder().positionNo(request.getPositionNo()).build())
                .build();

        log.info(String.valueOf(user));
        userRepository.save(user);
        return user.getUserNo();
    }

    public User loginUser(LoginUser request){
        Optional<User> userCheck = userRepository.findByEmailAndUserPw(request.getEmail(), request.getUserPw());
        if(userCheck.isEmpty()){
            //todo : 로그인 실패 화면으로 이동
            // throw new RuntimeException();
            return null;
        }
        //return userCheck.get();
        User user = userCheck.get();

        if (user.getDepartment() != null) {
            String deptName = user.getDepartment().getDeptName(); // 강제 초기화
        }
        /*if (user.getPosition() != null) {
            String positionName = user.getPosition().getPositionName(); // 강제 초기화
        }*/


        return user;
    }

    public List<UserDto> getColleagues() {
        List<User> users = userRepository.findAllByOrderByDepartment_DeptNo();

        return users.stream().map(UserMapper::userToDto).toList();
    }

    public List<UserDto> searchUser(String keyword) {
        // 1) 부서까지 한 번에 가져오기
        List<User> users = userRepository.searchWithDepartmentByUserNameContaining(keyword);
        List<UserDto> dtoList = new ArrayList<>();

        // 2) 필요한 PositionNo 미리 수집
        Set<String> positionNos = users.stream()
                .map(User::getPosition)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // 3) Position bulk 조회
        Map<String, Position> positionMap = positionRepository.findAllByPositionNoIn(positionNos)
                .stream()
                .collect(Collectors.toMap(Position::getPositionNo, Function.identity()));

        // 4) 변환
        for(User user : users){
            UserDto userDto = new UserDto();
            userDto.setUserNo(user.getUserNo());
            userDto.setUserName(user.getUserName());

            userDto.setDepartment(user.getDepartment() != null ? user.getDepartment().getDeptName() : null);

            Position position = positionMap.get(user.getPosition());
            userDto.setPosition(position != null ? position.getPositionName() : null);

            dtoList.add(userDto);
        }

        log.info(dtoList.toString());
        return dtoList;
    }





    @Autowired
    private DepartmentRepository departmentRepository;

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();  // 부서 전체 가져오기
    }


    public List<Position> getAllPositions() {
        return positionRepository.findAll();}
    /*public List<String> getAllPositions() {
        return userRepository.findDistinctPositionNames(); // ✅ 문자열만 반환하도록
    }*/

    /*public List<Position> getAllPositions() {
        return positionRepository.findAll(); // positionNo, positionName 모두 포함
    }*/


    @Transactional
    public void updateUser(UserUpdateRequest request, User sessionUser) {
        // DB에서 기존 유저 조회
        User user = userRepository.findById(sessionUser.getUserNo())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 필드 값 수정
        user.setUserName(request.getUserName());
        user.setEmail(request.getEmail());
        user.setAddress(request.getAddress());
        user.setBirthday(request.getBirthday());

        // 저장은 @Transactional에 의해 자동으로 commit 됨
    }





}






