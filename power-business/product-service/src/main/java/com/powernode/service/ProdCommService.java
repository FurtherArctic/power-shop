package com.powernode.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.powernode.domain.ProdComm;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author wangjunchen
 */
public interface ProdCommService extends IService<ProdComm> {

    /**
     * 多条件分页查询商品评论列表
     *
     * @param page     分页
     * @param prodComm 评论
     * @return page
     */
    Page<ProdComm> selectProdCommPage(Page<ProdComm> page, ProdComm prodComm);
}
