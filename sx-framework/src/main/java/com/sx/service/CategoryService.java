package com.sx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.domain.ResponseResult;
import com.sx.domain.entity.Category;
import com.sx.domain.vo.CategoryVo;

import java.util.List;


/**
 * 分类表(Category)表服务接口
 *
 * @author makejava
 * @since 2023-03-01 10:33:47
 */
public interface CategoryService extends IService<Category> {

    ResponseResult getCategoryList();

    List<CategoryVo> listAllCategory();

    ResponseResult pageListCategory(int pageNum, int pageSize, String name, String status);

    ResponseResult addCategory(CategoryVo categoryVo);


    ResponseResult getCategoryById(Long id);

    ResponseResult updateCategory(CategoryVo categoryVo);
}

