package com.powernode.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.powernode.domain.ProdTag;

/**
 * @author wangjunchen
 */
public interface ProdTagService extends IService<ProdTag> {
    /**
     * 多条件分页查询分组标签列表
     *
     * @param page    分页
     * @param prodTag 分页标签
     * @return 标签分页
     */

    Page<ProdTag> selectProdTagPage(Page<ProdTag> page, ProdTag prodTag);
}
