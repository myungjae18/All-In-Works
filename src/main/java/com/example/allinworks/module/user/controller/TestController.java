package com.example.allinworks.module.user.controller;

import com.example.allinworks.module.user.dto.JoinUserRequest;
import com.example.allinworks.module.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/test")
public class TestController {
    private UserService userService;

    /*데이터 입력 테스트*/
    @RequestMapping("/join")
    public ResponseEntity<?> joinUser(@RequestBody JoinUserRequest request) {
        return ResponseEntity.ok(userService.joinUser(request));
    }
}
