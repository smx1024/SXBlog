package com.sx.controller;

import com.sx.domain.ResponseResult;
import com.sx.domain.entity.Role;
import com.sx.domain.vo.RoleStatusVo;
import com.sx.domain.vo.RoleVo;
import com.sx.service.MenuService;
import com.sx.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/role")
public class RoleController {
    @Autowired
    RoleService roleService;
    @GetMapping("/list")
    public ResponseResult roleList(int pageNum, int pageSize, String roleName, String status){
        return  roleService.roleList(pageNum,pageSize,roleName,status);
    }
    @PutMapping("/changeStatus")
    public  ResponseResult updateRoleStatus(@RequestBody RoleStatusVo roleVo){
        return  roleService.updateRoleStatus(roleVo);
    }
    @PostMapping("")
    public  ResponseResult addRole(@RequestBody RoleVo roleVo){
        return  roleService.addRole(roleVo);
    }

    @GetMapping("/{id}")
    public ResponseResult getRole(@PathVariable("id") Long id){
        return  roleService.getRole(id);
    }
    @PutMapping("")
    public  ResponseResult updateRole(@RequestBody RoleVo roleVo){
        return  roleService.updateRole(roleVo);
    }
    @DeleteMapping("/{id}")
    public  ResponseResult deleteRole(@PathVariable("id") Long id){
        roleService.removeById(id);
        return  ResponseResult.okResult();
    }
    @GetMapping("/listAllRole")
    public  ResponseResult listAllRole(){
        List<Role> list = roleService.list();
        return  ResponseResult.okResult(list);
    }
}
