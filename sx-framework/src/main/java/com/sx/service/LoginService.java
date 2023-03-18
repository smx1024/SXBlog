package com.sx.service;

import com.sx.domain.ResponseResult;
import com.sx.domain.entity.User;

public interface LoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}
