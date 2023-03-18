package com.sx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.domain.ResponseResult;
import com.sx.domain.entity.User;
import org.springframework.stereotype.Service;


public interface BlogLoginService  {
    ResponseResult login(User user);
}
