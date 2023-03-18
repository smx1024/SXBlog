package com.sx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sx.domain.ResponseResult;
import com.sx.domain.entity.Role;
import com.sx.domain.entity.RoleMenu;
import com.sx.domain.vo.PageVo;
import com.sx.domain.vo.RoleStatusVo;
import com.sx.domain.vo.RoleVo;
import com.sx.mapper.RoleMapper;
import com.sx.service.RoleMenuService;
import com.sx.service.RoleService;
import com.sx.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author makejava
 * @since 2023-03-06 16:23:39
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
@Autowired
   RoleMenuService roleMenuService;


    @Override
    public List<String> selectRoleKeyByUserId(Long id) {
        //判断是否是管理员 如果是返回集合中只需要有admin
        if(id == 1L){
            List<String> roleKeys = new ArrayList<>();
            roleKeys.add("admin");
            return roleKeys;
        }
        //否则查询用户所具有的角色信息
        return getBaseMapper().selectRoleKeyByUserId(id);
    }

    @Override
    public ResponseResult roleList(int pageNum, int pageSize, String roleName, String status) {
        LambdaQueryWrapper<Role> queryWrapper=new LambdaQueryWrapper();
        queryWrapper.like(StringUtils.hasText(roleName),Role::getRoleName,roleName)
                .like(StringUtils.hasText(status),Role::getStatus,status).orderByAsc(Role::getRoleSort);
        Page<Role> page =new Page<>(pageNum,pageSize);
        page(page,queryWrapper);
        List<RoleVo> roleVos = BeanCopyUtils.copyBeanList(page.getRecords(), RoleVo.class);
        PageVo pageVo = new PageVo(roleVos,page.getTotal());
        return ResponseResult.okResult(pageVo);

    }

    @Override
    public ResponseResult updateRoleStatus(RoleStatusVo role) {
        LambdaUpdateWrapper<Role> updateWrapper =new LambdaUpdateWrapper<>();
        updateWrapper.eq(Role::getId,role.getRoleId()).set(Role::getStatus,role.getStatus());
        update(updateWrapper);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult addRole(RoleVo roleVo) {
        Role role = BeanCopyUtils.copyBean(roleVo, Role.class);
        save(role);
        List<RoleMenu> roleMenus = roleVo.getMenuIds().stream().map(menuId -> new RoleMenu(role.getId(), menuId)).collect(Collectors.toList());
        roleMenuService.saveBatch(roleMenus);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getRole(Long id) {
        Role role = getById(id);
        RoleVo roleVo = BeanCopyUtils.copyBean(role, RoleVo.class);
        return ResponseResult.okResult(roleVo);
    }

    @Override
    public ResponseResult updateRole(RoleVo roleVo) {
        List<Long> menuIds = roleVo.getMenuIds();
        Role role = BeanCopyUtils.copyBean(roleVo, Role.class);
        updateById(role);
        LambdaUpdateWrapper<RoleMenu> updateWrapper=new LambdaUpdateWrapper<>();
        updateWrapper.eq(RoleMenu::getRoleId,role.getId());
        roleMenuService.remove(updateWrapper);
        List<RoleMenu> roleMenus = menuIds.stream().map(menuId -> new RoleMenu(role.getId(), menuId)).collect(Collectors.toList());
        roleMenuService.saveBatch(roleMenus);
        return ResponseResult.okResult();
    }
}
