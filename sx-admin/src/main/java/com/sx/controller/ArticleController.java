package com.sx.controller;

import com.sx.domain.ResponseResult;
import com.sx.domain.dto.AddArticleDto;
import com.sx.domain.dto.GetArticleVo;
import com.sx.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @PostMapping
    public ResponseResult add(@RequestBody AddArticleDto article){
        return articleService.add(article);
    }
    @GetMapping("/list")
    public  ResponseResult atriclelikeList(int pageNum,int pageSize,String title,String summary){
        return   articleService.atriclelikeList(pageNum,pageSize,title,summary);
    }

    //修改文章之查询回显
    @GetMapping("/{id}")
    public  ResponseResult getArticle(@PathVariable("id") Long id){
        return   articleService.getArticle(id);
    }
    //修改文章之修改
    @PutMapping()
    public  ResponseResult updateArticle(@RequestBody GetArticleVo articleVo){
        return   articleService.updateArticle(articleVo);
    }
    @DeleteMapping("/{id}")
    public  ResponseResult deleteArticle(@PathVariable("id") Long id){
        return   articleService.deleteArticle(id);
    }



}
