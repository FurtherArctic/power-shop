package com.powernode.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.powernode.domain.ProdTag;
import com.powernode.mapper.ProdTagMapper;
import com.powernode.service.ProdTagService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * @author wangjunchen
 */
@Service
public class ProdTagServiceImpl extends ServiceImpl<ProdTagMapper, ProdTag> implements ProdTagService {

    @Resource
    private ProdTagMapper prodTagMapper;

    @Override
    public Page<ProdTag> selectProdTagPage(Page<ProdTag> page, ProdTag prodTag) {
        //如果like和eq同时存在的情况下，应该优先写eq方法，因为eq方法会创建索引，只要是用户输入的数据都用模糊查询like
        return prodTagMapper.selectPage(page, new LambdaQueryWrapper<ProdTag>()
                .eq(ObjectUtil.isNotEmpty(prodTag.getStatus()), ProdTag::getStatus, prodTag.getStatus())
                .like(StringUtils.hasText(prodTag.getTitle()), ProdTag::getTitle, prodTag.getTitle())
                .orderByDesc(ProdTag::getSeq)
        );
    }
}
