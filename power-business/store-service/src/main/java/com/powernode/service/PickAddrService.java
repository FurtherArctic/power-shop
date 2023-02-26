package com.powernode.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.powernode.domain.PickAddr;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author wangjunchen
 */
public interface PickAddrService extends IService<PickAddr> {

    /**
     * 多条件分页查询自提点列表
     *
     * @param page     分页
     * @param pickAddr 自提点
     * @return 自提点分页数据
     */
    Page<PickAddr> selectPickAddrPage(Page<PickAddr> page, PickAddr pickAddr);
}
