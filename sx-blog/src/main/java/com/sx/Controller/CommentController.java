package com.sx.Controller;

import com.sx.constants.SystemConstants;
import com.sx.domain.ResponseResult;
import com.sx.domain.entity.Comment;
import com.sx.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;
  @GetMapping("/commentList")
    public ResponseResult commentList(Long articleId ,Integer pageNum,Integer pageSize){
      return commentService.commentList(SystemConstants.ARTICLE_COMMENT,articleId,pageNum,pageSize);
  }
  @PostMapping
    public  ResponseResult addCommont(@RequestBody Comment comment){
      return  commentService.addComment(comment);
  }
    @GetMapping("/linkCommentList")
    public ResponseResult linkCommentList(Integer pageNum,Integer pageSize){
        return commentService.commentList(SystemConstants.LINK_COMMENT,null,pageNum,pageSize);
    }
}
