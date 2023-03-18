package com.sx.controller;

import com.sx.domain.ResponseResult;
import com.sx.domain.entity.Tag;
import com.sx.domain.vo.PageVo;
import com.sx.domain.dto.TagListDto;
import com.sx.domain.vo.TagVo;
import com.sx.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/content/tag")
public class TagController {
    @Autowired
    private TagService tagService;

    @GetMapping("/list")
    public ResponseResult<PageVo> list(Integer pageNum, Integer pageSize, TagListDto tagListDto){
        return tagService.pageTagList(pageNum,pageSize,tagListDto);
    }
    @PostMapping()
    public  ResponseResult addContent(@RequestBody Tag tag){
        return  tagService.addContent(tag);
    }
    @DeleteMapping("/{id}")
    public ResponseResult deleContent(@PathVariable("id") Long id){
        return tagService.deleContent(id);
    }
    @GetMapping("/{id}")
    public ResponseResult getContent(@PathVariable("id") Long id){
        return tagService.getContent(id);
    }
    @PutMapping()
    public  ResponseResult updateContent(@RequestBody TagListDto tagvo){

        return  tagService.updateContent(tagvo);
    }

    @GetMapping("/listAllTag")
    public ResponseResult listAllTag(){
        List<TagVo> list = tagService.listAllTag();
        return ResponseResult.okResult(list);
    }
}
