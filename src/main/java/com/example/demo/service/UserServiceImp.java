package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.exception.DuplicateRecordException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.dto.UserDto;
import com.example.demo.model.mapper.UserMapper;
import com.example.demo.model.request.CreateUserReq;
import com.example.demo.model.request.PasswordUpdateReq;
import com.example.demo.model.request.UpdateUserReq;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserServiceImp implements UserService{
    private static ArrayList<User> listuser = new ArrayList<>();

    static {
        listuser.add(new User(1,"hoang","hoang@gmail.com","12345678","a", PassEncoding("123456")));

        listuser.add(new User(2,"loc","loc@gmail.com","00000001","a", PassEncoding("loc123")));

        listuser.add(new User(3,"vu","vu@gmail.com","00000002","a", PassEncoding("vu123")));

        listuser.add(new User(4,"tung","tung@gmail.com","00000003","a", PassEncoding("tung123")));

        listuser.add(new User(5,"binh","binh@gmail.com","00000004","a", PassEncoding("binh546")));
    }

    //List User should be encoding the password
    private static String PassEncoding(String pass){
        return BCrypt.hashpw(pass, BCrypt.gensalt((int) (4 + (Math.random() * 6))));// random log_round
    }

    //Check password
    private boolean CheckPassword(String input, String pass){
        boolean valuate = BCrypt.checkpw(input, pass);
        return valuate;
    }

    @Override
    public List<User> allInfo() {
        return listuser;
    }

    @Override
    public List<UserDto> getListUser() {
        List<UserDto> result = new ArrayList<>();
        for (User item:
             listuser) {
            result.add(UserMapper.toUserDto(item));
        }
        return result;
    }

    @Override
    public UserDto getUser(int id) {
        for (User item:
             listuser) {
            if (item.getId() == id) return UserMapper.toUserDto(item);
        }
        throw new NotFoundException("User NOT_FOUND in system");
    }

    @Override
    public UserDto Login(String username, String password) {
        for (User item:
             listuser) {
            if (item.getName().equals(username) && CheckPassword(password, item.getPassword())){
                return UserMapper.toUserDto(item);
            }
        }
        throw new NotFoundException("Username or password is invalid");
    }

    @Override
    public UserDto CreateUser(CreateUserReq user) {
        for (User item:
                listuser) {
            if(item.getEmail().equals(user.getEmail())) {
                throw new DuplicateRecordException("Email is existed in system");
            }
        }
        User userDto = new User();
        userDto.setId(listuser.get(listuser.size()-1).getId() + 1);
        userDto.setName(user.getName());
        userDto.setPassword(PassEncoding(user.getPassword()));
        userDto.setEmail(user.getEmail());
        userDto.setPhone(user.getPhone());
        userDto.setAvatar("");

        listuser.add(userDto);
        return UserMapper.toUserDto(userDto);
    }

    @Override
    public UserDto UpdateUser(UpdateUserReq req, int id) {
        for (User user1:
             listuser) {
            if(user1.getId() == id){
                if(!req.getEmail().equals(user1.getEmail())){
                    for (User user2:
                         listuser) {
                        //Check email exist
                        if(req.getEmail().equals(user2.getEmail()))
                            throw new DuplicateRecordException("New Email already exists in the system");
                    }
                    user1.setEmail(req.getEmail());
                }
                user1.setName(req.getName());
                user1.setPhone(req.getPhone());
                user1.setAvatar(req.getAvatar());
                return UserMapper.toUserDto(user1);
            }
        }
        throw new NotFoundException("User NOT_FOUND");
    }

    @Override
    public UserDto UpdatePassword(PasswordUpdateReq req, int id) {
        for (User user:
             listuser){
            if(user.getId() == id){
                if(CheckPassword(req.getLast_password(), user.getPassword())){
                    if(req.getNew_password().equals(req.getConfirm_password())){
                        user.setPassword(PassEncoding(req.getConfirm_password()));
                        return UserMapper.toUserDto(user);
                    }
                    throw new DuplicateRecordException("Confirm-password not match");
                }
                throw new DuplicateRecordException("Password not match");
            }
        }
        throw new NotFoundException("User NOT_FOUND");
    }

    @Override
    public List<UserDto> DeleteUser(int id) {
        for (User user:
                listuser){
            if(user.getId() == id){
                listuser.remove(user);
                return getListUser();
            }
        }
        throw new NotFoundException("User NOT_FOUND");
    }
}


