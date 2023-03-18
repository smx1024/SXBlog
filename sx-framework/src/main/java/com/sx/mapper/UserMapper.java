package com.sx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sx.domain.entity.User;
import org.apache.ibatis.annotations.Mapper;


/**
 * 用户表(User)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-02 15:49:39
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}

