package com.sungwon.app.mapper;

import com.sungwon.app.dto.UserDTO;
import org.apache.catalina.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    void insertUser(UserDTO userDTO);

    UserDTO findUserById(String userid);
}