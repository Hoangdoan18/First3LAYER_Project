package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.dto.UserDto;
import com.example.demo.model.request.CreateUserReq;
import com.example.demo.model.request.PasswordUpdateReq;
import com.example.demo.model.request.UpdateUserReq;
import com.example.demo.model.request.UploadForm;
import com.example.demo.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.util.List;

@RequestMapping("/system")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    private static String UPLOAD_DIR = System.getProperty("user.home") + "/upload";


    @GetMapping("")
    public ResponseEntity<?> getFullListUser() {
        List<User> result = userService.allInfo();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    //API config
    @ApiOperation(value = "Get list user", response = UserDto.class, responseContainer = "List")
    @ApiResponses({
            @ApiResponse(code=500,message = "")
    })
    @GetMapping("/users")
    public ResponseEntity<?> getListUser() {
        List<UserDto> result = userService.getListUser();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }


    //LOGIN FOR USER
    @GetMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username,@RequestParam String password) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.Login(username,password));
    }


    //GET USER INFO
    @ApiOperation(value = "Get user info by id", response = UserDto.class)
    @ApiResponses({
            @ApiResponse(code=404,message = "No user found"),
            @ApiResponse(code=500,message = "")
    })
    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUser(@PathVariable int id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUser(id));
    }


    //CREATE USER
    @ApiOperation(value = "Create user", response = UserDto.class)
    @ApiResponses({
            @ApiResponse(code=400,message = "Email already exists in the system"),
            @ApiResponse(code=500,message = "")
    })
    @PostMapping("/create-user")
    public ResponseEntity<?> AddUser(@Valid @RequestBody CreateUserReq userReq){
        return ResponseEntity.status(HttpStatus.OK).body(userService.CreateUser(userReq));
    }


    //UPDATE USER PROFILE
    @PutMapping("/update-user/{id}")
    public ResponseEntity<?> UpdateUser(@Valid @RequestBody UpdateUserReq req, @PathVariable int id){
        return ResponseEntity.status(HttpStatus.OK).body(userService.UpdateUser(req,id));
    }


    //UPDATE PASSWORD
    @PutMapping("/change-password/{id}")
    public ResponseEntity<?> ChangeUserPassword(@Valid @RequestBody PasswordUpdateReq req, @PathVariable int id){
        return ResponseEntity.status(HttpStatus.OK).body(userService.UpdatePassword(req,id));
    }


    //DELETE USER
    @DeleteMapping("/delete-user/{id}")
    public ResponseEntity<?> DeleteUser(@PathVariable int id){
        return ResponseEntity.status(HttpStatus.OK).body(userService.DeleteUser(id));
    }


    //Spring Boot binding data from form request into param with @ModelAttribute
    @PostMapping("/upload/{id}")
    public ResponseEntity<?> uploadFile(@ModelAttribute("uploadForm")UploadForm form){
        //Create folder to save file if not exist(in this project we save it with link "/file/{id}/file_name")
        File uploadDir = new File(UPLOAD_DIR);
        if(!uploadDir.exists()){
            uploadDir.mkdirs();
        }

        //Get file data
        MultipartFile fileData = form.getFileData();
        String name = fileData.getOriginalFilename();
        if(name != null && name.length() > 0){
            try {
                //Create file
                File serverFile = new File(UPLOAD_DIR + "/" + name);
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
                //Read data
                stream.write(fileData.getBytes());
                stream.close();
                return ResponseEntity.ok("Upload success\n"+"File path: /file/" + name);
            }catch (Exception e){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error when uploading");
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("BAD_REQUEST");
    }

    @GetMapping("file/{filename}")
    public ResponseEntity<?> download(@PathVariable String filename){
        File file = new File(UPLOAD_DIR + "/" + filename);
        if (!file.exists()){
            throw new NotFoundException("File NOT_FOUND");
        }

        //Create URL resource for 'filename' (throw MalformedURLException)
        UrlResource resource;
        try {
            resource = new UrlResource(file.toURI());
        }catch (MalformedURLException e){
            throw new NotFoundException("File not found");
        }
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"").body(resource);
    }
}
