package com.sx.service.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.domain.ResponseResult;
import com.sx.domain.entity.LoginUser;
import com.sx.domain.entity.User;
import com.sx.domain.vo.BlogUserLoginVo;
import com.sx.domain.vo.UserInfoVo;
import com.sx.service.BlogLoginService;
import com.sx.utils.BeanCopyUtils;
import com.sx.utils.JwtUtil;
import com.sx.utils.RedisCache;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class BlogLoginServiceImpl  implements BlogLoginService {
   @Autowired
    AuthenticationManager authenticationManager;
   @Autowired
    RedisCache redisCache;
    @Override
    public ResponseResult login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //判断是否认证通过
        if (Objects.isNull(authenticate)){
            throw  new RuntimeException("用户名或密码错误123");
        }
        //获取userId 生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String id = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(id);
        //把用户数据存入redis
        redisCache.setCacheObject("bloglogin:"+id,loginUser);
        //吧token封装返回
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);
        BlogUserLoginVo loginVo = new BlogUserLoginVo(jwt,userInfoVo);
        return ResponseResult.okResult(loginVo);
    }
}
