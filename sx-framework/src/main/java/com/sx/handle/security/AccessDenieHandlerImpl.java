package com.sx.handle.security;

import com.alibaba.fastjson.JSON;
import com.sx.domain.ResponseResult;
import com.sx.enums.AppHttpCodeEnum;
import com.sx.utils.WebUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Component
public class AccessDenieHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
            e.printStackTrace();
            ResponseResult result=ResponseResult.errorResult(AppHttpCodeEnum.NO_OPERATOR_AUTH);
            //响应给前端
            WebUtils.renderString(httpServletResponse, JSON.toJSONString(result));

        }
}
