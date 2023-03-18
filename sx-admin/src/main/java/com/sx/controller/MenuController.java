package com.sx.controller;

import com.sx.domain.ResponseResult;
import com.sx.domain.entity.Menu;
import com.sx.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping("/system/menu")
public class MenuController {
    @Autowired
    MenuService menuService;

    //获取菜单，也可以模糊查找
    @GetMapping("/list")
    public ResponseResult menuList(@RequestParam(required = false) String status,@RequestParam(required = false) String menuName){
           return  menuService.menuList(status,menuName);
    }
    @PostMapping("")
    public  ResponseResult addMenu(@RequestBody Menu menu){
            return  menuService.addMenu(menu);
    }
    @GetMapping("/{id}")
    public  ResponseResult getMenuById(@PathVariable Long id){
            return  menuService.getMenuById(id);
    }
    @PutMapping("")
    public  ResponseResult updateMenu(@RequestBody Menu menu){
        return  menuService.updateMenu(menu);
    }
    @DeleteMapping("/{menuId}")
    public  ResponseResult deleteMenu(@PathVariable("menuId") Long id){
        return  menuService.deleteMenu(id);
    }
    @GetMapping("/treeselect")
    public  ResponseResult getRoleMenu(){
        return  menuService.getRoleMenu();
    }
     @GetMapping("/roleMenuTreeselect/{id}")
    public  ResponseResult getMenuTreeselect(@PathVariable("id") Long id){
        return  menuService.getMenuTreeselect(id);
    }

}
