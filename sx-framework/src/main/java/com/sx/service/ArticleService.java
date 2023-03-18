package com.sx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.domain.ResponseResult;
import com.sx.domain.dto.AddArticleDto;
import com.sx.domain.dto.GetArticleVo;
import com.sx.domain.entity.Article;

public interface ArticleService extends IService<Article> {
   ResponseResult hotArticleList() ;

   ResponseResult articleList(Integer pageNum, Integer pageSize, long categoryId);

   ResponseResult getArticleDeatil(Long id);

    ResponseResult updateViewCount(Long id);

    ResponseResult add(AddArticleDto article);

    ResponseResult atriclelikeList(int pageNum, int pageSize, String title, String summary);

    ResponseResult getArticle(Long id);

    ResponseResult updateArticle(GetArticleVo articleVo);

    ResponseResult deleteArticle(Long id);
}
