package com.sx.controller;

import com.sx.domain.ResponseResult;
import com.sx.domain.vo.LinkVo;
import com.sx.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/link")
public class LinkController {
    @Autowired
    LinkService linkService;
    @GetMapping("/list")
    public ResponseResult linkList(int pageNum,int pageSize,String name,String status ){
     return   linkService.linkList(pageNum,pageSize,name,status);
    }
    @PostMapping("")
    public  ResponseResult addLink(@RequestBody LinkVo linkVo){
        return  linkService.addLink(linkVo);
    }
    //更新链表之查询回显
    @GetMapping("/{id}")
    public ResponseResult getLinkById(@PathVariable("id") Long id){
        return  linkService.getLinkById(id);
    }
    //更新链表
    @PutMapping("")
    public ResponseResult updateLink(@RequestBody LinkVo linkVo){
        return  linkService.updateLink(linkVo);
    }
    @DeleteMapping("/{id}")
    public  ResponseResult deleteLink(@PathVariable("id") Long id){
        return linkService.deleteLink(id);
    }

}
