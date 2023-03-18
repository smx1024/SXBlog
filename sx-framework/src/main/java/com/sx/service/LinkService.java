package com.sx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sx.domain.ResponseResult;
import com.sx.domain.entity.Link;
import com.sx.domain.vo.LinkVo;


/**
 * 友链(Link)表服务接口
 *
 * @author makejava
 * @since 2023-03-02 15:18:09
 */
public interface LinkService extends IService<Link> {



    ResponseResult getAllLink();

    ResponseResult linkList(int pageNum, int pageSize, String name, String status);

    ResponseResult addLink(LinkVo linkVo);

    ResponseResult getLinkById(Long id);

    ResponseResult updateLink(LinkVo linkVo);

    ResponseResult deleteLink(Long id);
}

