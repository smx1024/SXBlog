package com.sx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.domain.ResponseResult;
import com.sx.domain.entity.Role;
import com.sx.domain.vo.RoleStatusVo;
import com.sx.domain.vo.RoleVo;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


/**
 * 角色信息表(Role)表服务接口
 *
 * @author makejava
 * @since 2023-03-06 16:23:39
 */
public interface RoleService extends IService<Role> {

    List<String> selectRoleKeyByUserId(Long id);
    ResponseResult  roleList(int pageNum, int pageSize, String roleName, String status);

    ResponseResult updateRoleStatus( RoleStatusVo roleVo);

    ResponseResult addRole(RoleVo roleVo);

    ResponseResult getRole(Long id);

    ResponseResult updateRole(RoleVo roleVo);
}

