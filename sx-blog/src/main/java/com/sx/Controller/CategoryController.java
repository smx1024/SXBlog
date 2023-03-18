package com.sx.Controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.sx.domain.ResponseResult;
import com.sx.domain.entity.Category;
import com.sx.domain.vo.ExcelCategoryVo;
import com.sx.enums.AppHttpCodeEnum;
import com.sx.service.CategoryService;
import com.sx.utils.BeanCopyUtils;
import com.sx.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/getCategoryList")
    public ResponseResult getCategoryList(){
       return categoryService.getCategoryList();
    }
}

    