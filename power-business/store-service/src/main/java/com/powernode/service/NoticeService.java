package com.powernode.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.powernode.domain.Notice;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author wangjunchen
 */
public interface NoticeService extends IService<Notice> {

    /**
     * 多条件分页查询公告列表
     *
     * @param page   分页
     * @param notice 公告
     * @return 公告分页查询结果
     */
    Page<Notice> selectNoticePage(Page<Notice> page, Notice notice);
}
