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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * 회원가입을 처리하는 메서드
     * 사용자 정보를 검증하고, 유효한 경우 데이터를 데이터베이스에 저장
     *
     * @param userDTO 사용자 정보가 담긴 DTO 객체
     */
    @Override
    public void insertUser(UserDTO userDTO) {
        // 중복 확인
        if (findUserById(userDTO.getUserid()) != null) {
            throw new IllegalArgumentException("User ID already exists");
        }
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword())); // 패스워드 암호화
        userMapper.insertUser(userDTO);
    }

    /**
     * 데이터베이스에서 주어진 사용자 ID에 해당하는 사용자 정보를 조회
     *
     * @param userId 조회할 사용자 ID
     * @return 사용자 정보가 담긴 UserDTO 객체, 사용자 ID가 존재하지 않으면 null 반환
     */
    @Override
    public UserDTO findUserById(String userId) {
        return userMapper.findUserById(userId);
    }

    /**
     * 사용자의 로그인 요청을 처리
     * 주어진 사용자 ID와 비밀번호를 인증하고, 인증 성공 시 사용자 정보를 반환
     *
     * @param userDTO 로그인 요청 정보가 담긴 UserDTO 객체
     * @return 인증된 사용자 정보가 담긴 UserDTO 객체
     */
    @Override
    public UserDTO login(UserDTO userDTO) {
        try {
            //Test
            UserDetails userDetails = userDetailsService.loadUserByUsername("user1");
            System.out.println("Loaded user: " + userDetails);
            // Authentication 객체 생성 및 인증
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDTO.getUserid(), userDTO.getPassword())
            );

            UserDTO user = userMapper.findUserById(userDTO.getUserid());
            SecurityContextHolder.getContext().setAuthentication(authentication); // 인증 정보 저장
            return user;
        } catch (AuthenticationException e) {
            e.printStackTrace(); // 구체적인 예외 메시지 확인
            throw new RuntimeException("Invalid username or password");
        }
    }

}
