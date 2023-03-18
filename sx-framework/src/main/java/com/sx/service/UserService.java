package com.sx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.domain.ResponseResult;
import com.sx.domain.entity.User;
import com.sx.domain.vo.UserVo;


/**
 * 用户表(User)表服务接口
 *
 * @author makejava
 * @since 2023-03-03 21:16:50
 */
public interface UserService extends IService<User> {

    ResponseResult userInfo();

    ResponseResult updateUserInfo(User user);

    ResponseResult register(User user);

    ResponseResult getUserList(int pageNum, int pageSize, String userName, String phonenumber, String status);

    ResponseResult addUser(UserVo userVo);

    ResponseResult getUserById(Long id);

    ResponseResult updateUser(UserVo userVo);
}

