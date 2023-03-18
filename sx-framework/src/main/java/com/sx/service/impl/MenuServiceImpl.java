package com.sx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sx.constants.SystemConstants;
import com.sx.domain.ResponseResult;
import com.sx.domain.entity.Menu;
import com.sx.domain.vo.Menus;
import com.sx.domain.vo.MenuListVo;
import com.sx.domain.vo.TreeSelect;
import com.sx.mapper.MenuMapper;
import com.sx.mapper.RoleMenuMapper;
import com.sx.service.MenuService;
import com.sx.service.RoleMenuService;
import com.sx.utils.BeanCopyUtils;
import com.sx.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author makejava
 * @since 2023-03-06 16:17:13
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    RoleMenuService roleMenuService;
    @Autowired
    MenuService menuService;
    @Autowired
    RoleMenuMapper roleMenuMapper;

    @Override
    public List<String> selectPermsByUserId(Long id) {
        //如果是管理员，返回所有的权限
        if (id == 1L) {
            LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(Menu::getMenuType, "c", "f");
            wrapper.eq(Menu::getStatus, SystemConstants.STATUS_NORMAL);
            List<Menu> menus = list(wrapper);
            List<String> perms = menus.stream()
                    .map(Menu::getPerms)
                    .collect(Collectors.toList());
            return perms;
        }
        //否则返回所具有的权限
        return getBaseMapper().selectPermsByUserId(id);
    }

    @Override
    public List<Menu> selectRouterMenuTreeByUserId(Long userId) {
        MenuMapper menuMapper = getBaseMapper();
        List<Menu> menus = null;
        //判断是否是管理员
        if (SecurityUtils.isAdmin()) {
            //如果是 获取所有符合要求的Menu
            menus = menuMapper.selectAllRouterMenu();
        } else {
            //否则  获取当前用户所具有的Menu
            menus = menuMapper.selectRouterMenuTreeByUserId(userId);
        }

        //构建tree
        //先找出第一层的菜单  然后去找他们的子菜单设置到children属性中
        List<Menu> menuTree = builderMenuTree(menus, 0L);
        return menuTree;

    }

    @Override
    public ResponseResult menuList(String status, String menuName) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(status), Menu::getStatus, status)
                .like(StringUtils.hasText(menuName), Menu::getMenuName, menuName)
                .orderByAsc(Menu::getParentId, Menu::getOrderNum);
        List<Menu> list = list(queryWrapper);
        List<MenuListVo> listVos = BeanCopyUtils.copyBeanList(list, MenuListVo.class);
        return ResponseResult.okResult(listVos);
    }

    @Override
    public ResponseResult addMenu(Menu menu) {
        save(menu);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getMenuById(Long id) {
        Menu menu = getById(id);
        MenuListVo listVo = BeanCopyUtils.copyBean(menu, MenuListVo.class);
        return ResponseResult.okResult(listVo);
    }

    @Override
    public ResponseResult updateMenu(Menu menu) {
        if (menu.getId().equals(menu.getParentId())) {
            return ResponseResult.errorResult(500, "修改菜单'写博文'失败，上级菜单不能选择自己");
        }
        updateById(menu);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteMenu(Long id) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getParentId, id);
        int size = list(queryWrapper).size();
        if (size != 0) {
            return ResponseResult.okResult().error(500, "存在子菜单不允许删除");
        }
        removeById(id);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getRoleMenu() {
        List<Menu> list = list();
        List<Menu> collect = list.stream().map(menu -> menu.setLabel(menu.getMenuName())).collect(Collectors.toList());
        List<Menu> menus = builderMenuTree(collect, 0);
        List<TreeSelect> treeSelects = BeanCopyUtils.copyBeanList(menus, TreeSelect.class);
//        treeSelects.stream().map(treeSelect -> treeSelect.setLabel(treeSelect.getMenuName())).map(treeSelect -> treeSelect.getChildren().stream().map(menu -> menu.setLabel(menu.getMenuName()))).collect(Collectors.toList());
        return ResponseResult.okResult(treeSelects);
    }

    @Override
    public ResponseResult getMenuTreeselect(Long id) {
        List<Menu> menus = null;
        //判断是否是管理员
        if (id==1) {
            //如果是 获取所有符合要求的Menu
                menus = list();

        } else {
            //否则  获取当前用户所具有的Menu
            menus= roleMenuMapper.getMenusByRoleId(id);
        }

        //构建tree
        //先找出第一层的菜单  然后去找他们的子菜单设置到children属性中
        List<Menu> collect = menus.stream().map(menu -> menu.setLabel(menu.getMenuName())).collect(Collectors.toList());
        List<Menu> menuTree = builderMenuTree(collect, 0L);
        return ResponseResult.okResult(new Menus(menuTree, null));
    }


    private List<Menu> builderMenuTree(List<Menu> menus, long parentId) {
        List<Menu> menuList = menus.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .map(menu -> menu.setChildren(getChildren(menu, menus)))
                .collect(Collectors.toList());
        return menuList;
    }

    private List<Menu> getChildren(Menu menu, List<Menu> menus) {
        List<Menu> menuList = menus.stream()
                .filter(m -> m.getParentId().equals(menu.getId()))
                .map(m -> m.setChildren(getChildren(m, menus)))
                .collect(Collectors.toList());
        return menuList;
    }
}










