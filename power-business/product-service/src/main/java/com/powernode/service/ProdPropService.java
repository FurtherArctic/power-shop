package com.powernode.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.powernode.domain.ProdProp;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author wangjunchen
 */
public interface ProdPropService extends IService<ProdProp> {

    /**
     * 多条件分页查询商品规格列表
     *
     * @param page     page
     * @param prodProp prodProp
     * @return page
     */
    Page<ProdProp> selectProdSpecPage(Page<ProdProp> page, ProdProp prodProp);
}
