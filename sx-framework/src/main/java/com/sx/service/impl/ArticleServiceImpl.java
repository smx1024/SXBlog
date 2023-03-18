package com.sx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sx.constants.SystemConstants;
import com.sx.domain.ResponseResult;
import com.sx.domain.dto.AddArticleDto;
import com.sx.domain.dto.ArticlelikeDto;
import com.sx.domain.dto.GetArticleVo;
import com.sx.domain.entity.Article;
import com.sx.domain.entity.ArticleTag;
import com.sx.domain.entity.Category;
import com.sx.domain.vo.ArticleDetailVo;
import com.sx.domain.vo.ArticleListVo;
import com.sx.domain.vo.HotArticleVo;
import com.sx.domain.vo.PageVo;
import com.sx.mapper.ArticleMapper;
import com.sx.mapper.ArticleTagMapper;
import com.sx.service.ArticleService;
import com.sx.service.ArticleTagService;
import com.sx.service.CategoryService;
import com.sx.utils.BeanCopyUtils;
import com.sx.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author 尚旭
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper,Article> implements ArticleService {
    @Autowired
    CategoryService categoryService;
    @Autowired
    RedisCache redisCache;
    @Autowired
    private ArticleTagService articleTagService;
    @Autowired
    private  ArticleService articleService;
    @Override
    public ResponseResult hotArticleList() {
        //查询热门文章并返回
        LambdaQueryWrapper<Article> queryWrapper=new LambdaQueryWrapper<>();
        //必须是正式文章
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        //按照浏览量排序
        queryWrapper.orderByDesc(Article::getViewCount);
        //只要前十个
        Page<Article> page=new Page(1,10);
        page(page,queryWrapper);
        List<Article> articles = page.getRecords();
//        bean 拷贝

//        List<HotArticleVo> list=new ArrayList<>();
//        for (Article article:articles){
//            HotArticleVo vo=new HotArticleVo();
//            BeanUtils.copyProperties(article,vo);
//            list.add(vo);
//        }
        List<HotArticleVo> vs= BeanCopyUtils.copyBeanList(articles,HotArticleVo.class);
        return ResponseResult.okResult(vs);
    }

    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, long categoryId) {
        //查询条件
        LambdaQueryWrapper<Article> queryWrapper=new LambdaQueryWrapper<>();

        //如果有categoryId就要在查询时要和传入时的相同
        queryWrapper.eq(Objects.nonNull(categoryId)&&categoryId>0,Article::getCategoryId,categoryId);
        //状态是正式发布的
        queryWrapper.eq(Article::getStatus,SystemConstants.ARTICLE_STATUS_NORMAL);
        //对isTop进行降序
        queryWrapper.orderByDesc(Article::getIsTop);
        //分页查询
        Page<Article> page=new Page<>(pageNum,pageSize);
        page(page,queryWrapper);

        List<Article> articles = page.getRecords();
        //查询categoryName
        articles.stream()
                .map(article ->
                        article.setCategoryName(categoryService.getById(article.getCategoryId()).getName()))
                .collect(Collectors.toList());
        //articleId去查询articleName进行设置
//        for (Article article : articles) {
//            Category category = categoryService.getById(article.getCategoryId());
//            article.setCategoryName(category.getName());
//        }

        //封装查询结果
        List<ArticleListVo> vos = BeanCopyUtils.copyBeanList(page.getRecords(), ArticleListVo.class);
        PageVo pageVo=new PageVo(vos,page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult getArticleDeatil(Long id) {
        //根据id查寻文章
        Article article = getById(id);
        //从redis中获取viewCount
        Integer viewCount = redisCache.getCacheMapValue("article:viewCount", id.toString());
        article.setViewCount(viewCount.longValue());
        //转换为VO
        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);
        //根据分类id查询分类名
        Long categoryId = articleDetailVo.getCategoryId();
        Category category = categoryService.getById(categoryId);
        if(category!=null){
            articleDetailVo.setCategoryName(category.getName());
        }
        //封装响应返回
        return ResponseResult.okResult(articleDetailVo);
    }

    @Override
    public ResponseResult updateViewCount(Long id) {
        //更新redis中对应 id的浏览量
        redisCache.incrementCacheMapValue("article:viewCount",id.toString(),1);
        return ResponseResult.okResult();
    }
    @Override
    @Transactional
    public ResponseResult add(AddArticleDto articleDto) {
        //添加 博客
        Article article = BeanCopyUtils.copyBean(articleDto, Article.class);
        save(article);
        List<ArticleTag> articleTags = articleDto.getTags().stream()
                .map(tagId -> new ArticleTag(article.getId(), tagId))
                .collect(Collectors.toList());

        //添加 博客和标签的关联
        articleTagService.saveBatch(articleTags);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult atriclelikeList(int pageNum, int pageSize, String title, String summary) {
        LambdaQueryWrapper<Article> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(title),Article::getTitle,title).like(StringUtils.hasText(summary),Article::getSummary,summary);
        Page<Article> page=new Page<>(pageNum,pageSize);
         page(page, queryWrapper);
        List<ArticlelikeDto> articlelikeDtos = BeanCopyUtils.copyBeanList(page.getRecords(), ArticlelikeDto.class);
        PageVo pageVo=new PageVo(articlelikeDtos,page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult getArticle(Long id) {
        Article article = getById(id);
        GetArticleVo articleVo = BeanCopyUtils.copyBean(article, GetArticleVo.class);
        LambdaQueryWrapper<ArticleTag> queryWrapper=new LambdaQueryWrapper();
        queryWrapper.eq(ArticleTag::getArticleId,id).select(ArticleTag::getTagId);
        List<ArticleTag> list = articleTagService.list(queryWrapper);
        List<Long> tagId = list.stream().map(tag -> tag.getTagId()).collect(Collectors.toList());
        articleVo.setTags(tagId);
        return ResponseResult.okResult(articleVo);
    }

    @Override
    public ResponseResult updateArticle(GetArticleVo articleVo) {
        Article article = BeanCopyUtils.copyBean(articleVo, Article.class);
        updateById(article);
        LambdaQueryWrapper<ArticleTag> queryWrapper=new LambdaQueryWrapper();
        queryWrapper.eq(ArticleTag::getArticleId,articleVo.getId());
        // 因为主键的问题更新和保存同时进行的批量操作还不太会，所以先删除，在统一保存
        articleTagService.remove(queryWrapper);
        //更新artcile与tag的关联
        List<ArticleTag> articleTags = articleVo.getTags().stream()
                .map(tagId -> new ArticleTag(article.getId(), tagId))
                .collect(Collectors.toList());
        //添加 博客和标签的关联
        articleTagService.saveBatch(articleTags);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteArticle(Long id) {
        articleService.removeById(id);
        return ResponseResult.okResult();
    }
}
