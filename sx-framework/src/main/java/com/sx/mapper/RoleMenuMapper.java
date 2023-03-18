package com.sx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sx.domain.entity.Menu;
import com.sx.domain.entity.RoleMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * 角色和菜单关联表(RoleMenu)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-10 20:12:22
 */
@Mapper
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {
    List<Menu> getMenusByRoleId(Long roleId);
}

