package com.sungwon.app.service;


import com.sungwon.app.dto.UserDTO;
import com.sungwon.app.mapper.UserMapper;

public interface UserService {
    void insertUser(UserDTO userDTO);

    UserDTO findUserById(String userId);

    UserDTO login(UserDTO userDTO);
}
