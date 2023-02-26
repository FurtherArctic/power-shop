package com.powernode.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.powernode.domain.IndexImg;

/**
 * @author wangjunchen
 */
public interface IndexImgService extends IService<IndexImg> {


    /**
     * 分页查询轮播图
     *
     * @param page     分页
     * @param indexImg 轮播图
     * @return 轮播图分页数据
     */
    Page<IndexImg> selectIndexImgPage(Page<IndexImg> page, IndexImg indexImg);
}
