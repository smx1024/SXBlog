package com.sx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sx.domain.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;


import java.util.List;


/**
 * 角色信息表(Role)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-06 16:23:39
 */
public interface RoleMapper extends BaseMapper<Role> {

    @Select(" SELECT\n" +
            "            r.`role_key`\n" +
            "        FROM\n" +
            "            `sys_user_role` ur\n" +
            "            LEFT JOIN `sys_role` r ON ur.`role_id` = r.`id`\n" +
            "        WHERE\n" +
            "            ur.`user_id` = #{userId} AND\n" +
            "            r.`status` = 0 AND\n" +
            "            r.`del_flag` = 0")
    List<String> selectRoleKeyByUserId(Long id);
}

