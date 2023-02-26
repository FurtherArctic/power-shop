package com.powernode.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.powernode.constant.NoticeConstant;
import com.powernode.domain.Notice;
import com.powernode.mapper.NoticeMapper;
import com.powernode.service.NoticeService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author wangjunchen
 */
@Service
@CacheConfig(cacheNames = "com.powernode.service.impl.NoticeServiceImpl")
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {
    @Resource
    private NoticeMapper noticeMapper;

    @Override
    public Page<Notice> selectNoticePage(Page<Notice> page, Notice notice) {
        return noticeMapper.selectPage(page, new LambdaQueryWrapper<Notice>()
                .eq(ObjectUtil.isNotEmpty(notice.getStatus()), Notice::getStatus, notice.getStatus())
                .eq(ObjectUtil.isNotEmpty(notice.getIsTop()), Notice::getIsTop, notice.getIsTop())
                .like(StringUtils.hasText(notice.getTitle()), Notice::getTitle, notice.getTitle())
                .orderByDesc(Notice::getIsTop, Notice::getPublishTime)
        );
    }

    @Override
    @CacheEvict(key = NoticeConstant.FRONT_NOTICE_LIST)
    public boolean save(Notice notice) {
        notice.setShopId(1L);
        notice.setUpdateTime(new Date());
        if (1 == notice.getStatus()) {
            notice.setPublishTime(new Date());
        }
        return noticeMapper.insert(notice) > 0;
    }

    @Override
    @CacheEvict(key = NoticeConstant.FRONT_NOTICE_LIST)
    public boolean updateById(Notice notice) {
        notice.setUpdateTime(new Date());
        return noticeMapper.updateById(notice) > 0;
    }
}
