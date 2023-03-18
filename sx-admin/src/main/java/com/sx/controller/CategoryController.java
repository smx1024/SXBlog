package com.sx.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.sx.domain.ResponseResult;
import com.sx.domain.entity.Category;
import com.sx.domain.vo.CategoryVo;
import com.sx.domain.vo.ExcelCategoryVo;
import com.sx.enums.AppHttpCodeEnum;
import com.sx.service.CategoryService;
import com.sx.utils.BeanCopyUtils;
import com.sx.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/content/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    
    @GetMapping("/listAllCategory")
    public ResponseResult listAllCategory(){
        List<CategoryVo> list = categoryService.listAllCategory();
        return ResponseResult.okResult(list);
    }

    @PreAuthorize("@ps.hasPermission('content:category:export')")
    @GetMapping("/export")
    public void export(HttpServletResponse response) {
        try {
            //设置下载文件的请求头
            WebUtils.setDownLoadHeader("分类.xlsx", response);
            //获取需要导出的数据
            List<Category> categoryVos = categoryService.list();

            List<ExcelCategoryVo> excelCategoryVos = BeanCopyUtils.copyBeanList(categoryVos, ExcelCategoryVo.class);
            //把数据写入到Excel中
            EasyExcel.write(response.getOutputStream(), ExcelCategoryVo.class).autoCloseStream(Boolean.FALSE).sheet("分类导出")
                    .doWrite(excelCategoryVos);

        } catch (Exception e) {
            //如果出现异常也要响应json
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            WebUtils.renderString(response, JSON.toJSONString(result));
        }
    }
    @GetMapping("/list")
    public  ResponseResult pageListCategory(int pageNum,int pageSize,String name,String status){
        return categoryService.pageListCategory(pageNum,pageSize,name,status);
    }
    @PostMapping("")
    public  ResponseResult addCategory(@RequestBody CategoryVo categoryVo){
        return categoryService.addCategory(categoryVo);
    }
    @GetMapping("/{id}")
    public  ResponseResult getCategoryById(@PathVariable("id") Long id){
        return categoryService.getCategoryById(id);
    }
    @PutMapping("")
    public  ResponseResult updateCategory(@RequestBody CategoryVo categoryVo){
        return  categoryService.updateCategory(categoryVo);
    }
    @DeleteMapping("/{id}")
    public  ResponseResult deleteCategory(@PathVariable("id") Long id){
        categoryService.removeById(id);
        return  ResponseResult.okResult();
    }
}

