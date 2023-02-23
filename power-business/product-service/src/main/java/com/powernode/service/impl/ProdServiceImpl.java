package com.powernode.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.powernode.domain.Prod;
import com.powernode.mapper.ProdMapper;
import com.powernode.service.ProdService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * @author wangjunchen
 */
@Service
public class ProdServiceImpl extends ServiceImpl<ProdMapper, Prod> implements ProdService {
    @Resource
    private ProdMapper prodMapper;

    /**
     * 当eq和like同时出现时，先用eq再用like
     *
     * @param page 分页
     * @param prod 商品信息
     * @return Page<Prod>
     */
    @Override
    public Page<Prod> selectProdPage(Page<Prod> page, Prod prod) {
        return prodMapper.selectPage(page, new LambdaQueryWrapper<Prod>()
                .eq(ObjectUtil.isNotEmpty(prod.getStatus()), Prod::getStatus, prod.getStatus())
                .like(StringUtils.hasText(prod.getProdName()), Prod::getProdName, prod.getProdName())
                .orderByDesc(Prod::getPutawayTime, Prod::getCreateTime)
        );
    }
}
