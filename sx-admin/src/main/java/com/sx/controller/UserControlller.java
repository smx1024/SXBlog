package com.sx.controller;

import com.sx.domain.ResponseResult;
import com.sx.domain.entity.User;
import com.sx.domain.vo.UserVo;
import com.sx.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/user")
public class UserControlller {
    @Autowired
    UserService userService;
    @GetMapping("/list")
    public ResponseResult getUserList(int pageNum, int pageSize, String userName,String phonenumber, String status){
        return userService.getUserList(pageNum,pageSize,userName,phonenumber,status);
    }
    @PostMapping("")
    public  ResponseResult addUser(@RequestBody UserVo userVo){
        return  userService.addUser(userVo);
    }
    @DeleteMapping("/{id}")
    public  ResponseResult deleteUser(@PathVariable("id")Long id){
        userService.removeById(id);
        return ResponseResult.okResult();
    }
    @GetMapping("/{id}")
    public ResponseResult getUserById(@PathVariable("id")Long id){
        return userService.getUserById(id);
    }
    @PutMapping("")
    public  ResponseResult updateUser(@RequestBody UserVo userVo){
        return  userService.updateUser(userVo);
    }

}
