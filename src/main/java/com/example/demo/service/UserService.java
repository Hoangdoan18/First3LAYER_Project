package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.model.dto.UserDto;
import com.example.demo.model.request.CreateUserReq;
import com.example.demo.model.request.PasswordUpdateReq;
import com.example.demo.model.request.UpdateUserReq;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    List<User> allInfo();
    List<UserDto> getListUser();
    UserDto getUser(int id);
    UserDto Login(String username, String password);
    UserDto CreateUser(CreateUserReq user);
    UserDto UpdateUser(UpdateUserReq user, int id);
    UserDto UpdatePassword(PasswordUpdateReq user, int id);
    List<UserDto> DeleteUser(int id);
}
