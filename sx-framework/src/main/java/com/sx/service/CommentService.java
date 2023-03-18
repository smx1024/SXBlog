package com.sx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.domain.ResponseResult;
import com.sx.domain.entity.Comment;


/**
 * 评论表(Comment)表服务接口
 *
 * @author makejava
 * @since 2023-03-03 20:42:00
 */
public interface CommentService extends IService<Comment> {

    ResponseResult commentList(String commentType, Long articleId, Integer pageNum, Integer pageSize);

    ResponseResult addComment(Comment comment);
}

