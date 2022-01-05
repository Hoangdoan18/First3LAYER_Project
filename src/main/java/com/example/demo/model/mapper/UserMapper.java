package com.example.demo.model.mapper;

import com.example.demo.entity.User;
import com.example.demo.model.dto.UserDto;

public class UserMapper {
    public static UserDto toUserDto(User user){
        UserDto udto = new UserDto();
        udto.setId(user.getId());
        udto.setName(user.getName());
        udto.setEmail(user.getEmail());
        udto.setPhone(user.getPhone());
        udto.setAvatar(user.getAvatar());
        return udto;
    }
}
