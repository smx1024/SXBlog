package com.sx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.domain.ResponseResult;
import com.sx.domain.entity.Tag;
import com.sx.domain.vo.PageVo;
import com.sx.domain.dto.TagListDto;
import com.sx.domain.vo.TagVo;

import java.util.List;


/**
 * 标签(Tag)表服务接口
 *
 * @author makejava
 * @since 2023-03-05 17:11:58
 */
public interface TagService extends IService<Tag> {

    ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto);

    ResponseResult addContent(Tag tag);

    ResponseResult deleContent(Long id);

    ResponseResult getContent(Long id);

    ResponseResult updateContent(TagListDto tagvo);

    List<TagVo> listAllTag();
}

