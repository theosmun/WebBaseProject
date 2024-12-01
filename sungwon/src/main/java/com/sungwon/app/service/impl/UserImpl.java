package com.sungwon.app.service.impl;

import com.sungwon.app.dto.UserDTO;
import com.sungwon.app.mapper.UserMapper;
import com.sungwon.app.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
@Log4j2
public class UserImpl implements UserService {
    @Autowired
    private AuthenticationManager authenticationManager;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void insertUser(UserDTO userDTO) {
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));  // 패스워드 암호화
        userMapper.insertUser(userDTO);
    }

    @Override
    public UserDTO findUserById(String userId) {
        return userMapper.findUserById(userId);
    }

    @Override
    public UserDTO login(UserDTO userDTO) {
        try {
            // Authentication 객체 생성 및 인증
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDTO.getUserid(), userDTO.getPassword())
            );

            // 인증 성공 시 사용자 정보 반환
            UserDTO user = userMapper.findUserById(userDTO.getUserid());
            SecurityContextHolder.getContext().setAuthentication(authentication); // 인증 정보 저장
            return user;
        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid username or password");
        }
    }

}
