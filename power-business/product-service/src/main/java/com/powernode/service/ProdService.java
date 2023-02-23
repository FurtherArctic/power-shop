package com.powernode.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.powernode.domain.Prod;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author wangjunchen
 */
public interface ProdService extends IService<Prod> {

    /**
     * 多条件分页查询商品列表
     *
     * @param page 分页
     * @param prod 商品信息
     * @return 商品信息集合信息
     */

    Page<Prod> selectProdPage(Page<Prod> page, Prod prod);
}
