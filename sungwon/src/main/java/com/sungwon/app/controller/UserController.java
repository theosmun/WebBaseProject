package com.sungwon.app.controller;

import com.sungwon.app.dto.UserDTO;
import com.sungwon.app.service.UserService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Data
@Slf4j
@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserDTO user) {
        log.info("Received UserDTO: {}", user.getUserid()); // 로그 출력
        log.info("Received UserDTO: {}", user.getPassword()); // 로그 출력
        userService.insertUser(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDTO user) {
        try {
            userService.login(user); // 로그인 처리
            return ResponseEntity.ok("User login successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
