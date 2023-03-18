package com.sx.Controller;

import com.sx.domain.ResponseResult;
import com.sx.domain.entity.LoginUser;
import com.sx.domain.entity.User;
import com.sx.service.BlogLoginService;
import com.sx.utils.JwtUtil;
import com.sx.utils.RedisCache;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
public class BLogLoginController {
    @Autowired
    private BlogLoginService blogLoginService;
    @Autowired
    RedisCache redisCache;
    @PostMapping("/login")
    public ResponseResult login(@RequestBody User user){
        if (!StringUtils.hasText(user.getPassword())){
                throw new RuntimeException("asd");
        }
        return blogLoginService.login(user);
    }
    @PostMapping("/logout")
    public ResponseResult logout(){
        //获取token，解析获取Userid
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser principal = (LoginUser) authentication.getPrincipal();
        //获取用户ID
        Long id = principal.getUser().getId();
        //删除redis的用户信息
        redisCache.deleteObject("bloglogin:"+id);
        return ResponseResult.okResult();
    }
}
