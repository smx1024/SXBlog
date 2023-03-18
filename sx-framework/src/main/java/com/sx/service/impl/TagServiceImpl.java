package com.sx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sx.domain.ResponseResult;
import com.sx.domain.entity.Tag;
import com.sx.domain.vo.PageVo;
import com.sx.domain.dto.TagListDto;
import com.sx.domain.vo.TagVo;
import com.sx.mapper.TagMapper;
import com.sx.service.TagService;
import com.sx.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 标签(Tag)表服务实现类
 *
 * @author makejava
 * @since 2023-03-05 17:11:58
 */
@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Override
    public ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto) {
        LambdaQueryWrapper<Tag> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasText(tagListDto.getName()),Tag::getName,tagListDto.getName());
        queryWrapper.eq(StringUtils.hasText(tagListDto.getRemark()),Tag::getRemark,tagListDto.getRemark());
        Page<Tag> page=new Page<>(pageNum,pageSize);
        page(page, queryWrapper);
        PageVo pageVo = new PageVo(page.getRecords(),page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult addContent(Tag tag) {
        save(tag);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleContent(Long id) {
        LambdaUpdateWrapper<Tag> updateWrapper=new LambdaUpdateWrapper();
        updateWrapper.eq(Tag::getId,id).set(Tag::getDelFlag,1);
        update(updateWrapper);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getContent(Long id) {
        Tag tag = getById(id);
        TagListDto tagDto = BeanCopyUtils.copyBean(tag, TagListDto.class);
        return ResponseResult.okResult(tagDto);
    }

    @Override
    public ResponseResult updateContent(TagListDto tagvo) {
        LambdaUpdateWrapper<Tag> updateWrapper =new LambdaUpdateWrapper();
        updateWrapper.eq(Tag::getId,tagvo.getId()).set(Tag::getName,tagvo.getName()).set(Tag::getRemark,tagvo.getRemark());
        update(updateWrapper);
        return ResponseResult.okResult();
    }

    @Override
    public List<TagVo> listAllTag() {
        LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Tag::getId,Tag::getName);
        List<Tag> list = list(wrapper);
        List<TagVo> tagVos = BeanCopyUtils.copyBeanList(list, TagVo.class);
        return tagVos;
    }
}
