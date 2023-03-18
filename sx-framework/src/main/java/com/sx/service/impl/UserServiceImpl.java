package com.sx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sx.domain.ResponseResult;
import com.sx.domain.entity.Role;
import com.sx.domain.entity.User;
import com.sx.domain.entity.UserRole;
import com.sx.domain.vo.PageVo;
import com.sx.domain.vo.UserInfoVo;
import com.sx.domain.vo.UserVo;
import com.sx.domain.vo.Userbyid;
import com.sx.enums.AppHttpCodeEnum;
import com.sx.exception.SystemException;
import com.sx.mapper.UserMapper;
import com.sx.service.RoleService;
import com.sx.service.UserRoleService;
import com.sx.service.UserService;
import com.sx.utils.BeanCopyUtils;
import com.sx.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2023-03-03 21:16:51
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
@Autowired
    UserRoleService userRoleService;
@Autowired
    RoleService roleService;
    @Override
    public ResponseResult userInfo() {
        Long userId = SecurityUtils.getUserId();
        User user = getById(userId);
        UserInfoVo userInfoVo= BeanCopyUtils.copyBean(user,UserInfoVo.class);
        return ResponseResult.okResult(userInfoVo);
    }

    @Override
    public ResponseResult updateUserInfo(User user) {
        updateById(user);
        return ResponseResult.okResult();
    }

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public ResponseResult register(User user) {
        //对数据进行非空判断
        if(!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getPassword())){
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        //对数据进行是否存在的判断
        if(userNameExist(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if(emailExist(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }
        //...
        //对密码进行加密
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        //存入数据库
        save(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getUserList(int pageNum, int pageSize, String userName, String phonenumber, String status) {
       LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper();
       queryWrapper.like(StringUtils.hasText(userName),User::getUserName,userName)
               .like(StringUtils.hasText(phonenumber),User::getPhonenumber,phonenumber)
               .eq(StringUtils.hasText(status),User::getStatus,status);
        Page<User> page=new Page<>(pageNum,pageSize);
        Page<User> page1 = page(page, queryWrapper);
        List<UserVo> userVoList = BeanCopyUtils.copyBeanList(page.getRecords(), UserVo.class);
        PageVo pageVo = new PageVo(userVoList, page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult addUser(UserVo userVo) {
        List<Long> roleIds = userVo.getRoleIds();
        User user = BeanCopyUtils.copyBean(userVo, User.class);
        //对数据进行非空判断
        if(!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        //对数据进行是否存在的判断
        if(userNameExist(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if(emailExist(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }
        if(phoneExist(user.getPhonenumber())){
            throw new SystemException(AppHttpCodeEnum.PHONENUMBER_EXIST);
        }
        //...
        //对密码进行加密
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        //存入数据库
        save(user);
        List<UserRole> roleList = roleIds.stream().map(roleId -> new UserRole(user.getId(), roleId)).collect(Collectors.toList());
        userRoleService.saveBatch(roleList);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getUserById(Long id) {
            LambdaQueryWrapper<UserRole> queryWrapper=new LambdaQueryWrapper<>();
            queryWrapper.eq(UserRole::getUserId,id);
        List<UserRole> userRoleList = userRoleService.list(queryWrapper);
        List<Long> roleIds= userRoleList.stream().map(userRole -> userRole.getRoleId()).collect(Collectors.toList());
        List<Role> roleList = roleService.list();
        User user = getById(id);
        UserInfoVo vo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        return ResponseResult.okResult(new Userbyid(roleIds,roleList,vo));
    }

    @Override
    public ResponseResult updateUser(UserVo userVo) {
        List<Long> roleIds = userVo.getRoleIds();
        User user = BeanCopyUtils.copyBean(userVo, User.class);
        updateById(user);
        LambdaUpdateWrapper<UserRole> updateWrapper=new LambdaUpdateWrapper<>();
        updateWrapper.eq(UserRole::getUserId,user.getId());
        userRoleService.remove(updateWrapper);
        List<UserRole> userRoleList = roleIds.stream().map(roleId -> new UserRole(user.getId(), roleId)).collect(Collectors.toList());
        userRoleService.saveBatch(userRoleList);
        return ResponseResult.okResult();
    }

    private boolean phoneExist(String phonenumber) {
        LambdaQueryWrapper<User> queryWrapper= new LambdaQueryWrapper();
        queryWrapper.eq(User::getPhonenumber,phonenumber);
        return count(queryWrapper)>0;
    }
    private boolean userNameExist(String userName) {
        LambdaQueryWrapper<User> queryWrapper= new LambdaQueryWrapper();
        queryWrapper.eq(User::getUserName,userName);
        return count(queryWrapper)>0;
    }
    private boolean emailExist(String email) {
        LambdaQueryWrapper<User> queryWrapper= new LambdaQueryWrapper();
        queryWrapper.eq(User::getEmail,email);
        return count(queryWrapper)>0;
    }

}
