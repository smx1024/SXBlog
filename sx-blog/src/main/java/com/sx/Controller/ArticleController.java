package com.sx.Controller;

import com.sx.domain.ResponseResult;
import com.sx.domain.entity.Article;
import com.sx.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    ArticleService articleService;

//    @RequestMapping("/")
//    public List<Article> test(){
//        return articleService.list();
//    }
    @GetMapping("/hotArticleList")
    public ResponseResult hotArticleList(){
        ResponseResult result=articleService.hotArticleList();
        return result;
    }
    @GetMapping("/articleList")
    public ResponseResult articleList(Integer pageNum,Integer pageSize,long categoryId){
        ResponseResult result=articleService.articleList(pageNum,pageSize,categoryId);
        return result;
    }  @GetMapping("/{id}")
    public  ResponseResult getArticleDeatil(@PathVariable("id") Long id){

        return articleService.getArticleDeatil(id);
    }

    @PutMapping("/updateViewCount/{id}")
    public ResponseResult updateViewCount(@PathVariable("id") Long id){
        return articleService.updateViewCount(id);
    }
}
