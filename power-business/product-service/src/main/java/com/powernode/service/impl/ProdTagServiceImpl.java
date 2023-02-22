package com.powernode.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.powernode.constant.TagConstant;
import com.powernode.domain.ProdTag;
import com.powernode.mapper.ProdTagMapper;
import com.powernode.service.ProdTagService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Date;

/**
 * @author wangjunchen
 */
@Service
@CacheConfig(cacheNames = "com.powernode.service.impl.ProdTagServiceImpl")
public class ProdTagServiceImpl extends ServiceImpl<ProdTagMapper, ProdTag> implements ProdTagService {

    @Resource
    private ProdTagMapper prodTagMapper;

    /**
     * 分页查询
     *
     * @param page    分页
     * @param prodTag 分页标签
     * @return select count
     */
    @Override
    public Page<ProdTag> selectProdTagPage(Page<ProdTag> page, ProdTag prodTag) {
        //如果like和eq同时存在的情况下，应该优先写eq方法，因为eq方法会创建索引，只要是用户输入的数据都用模糊查询like
        return prodTagMapper.selectPage(page, new LambdaQueryWrapper<ProdTag>()
                .eq(ObjectUtil.isNotEmpty(prodTag.getStatus()), ProdTag::getStatus, prodTag.getStatus())
                .like(StringUtils.hasText(prodTag.getTitle()), ProdTag::getTitle, prodTag.getTitle())
                .orderByDesc(ProdTag::getSeq)
        );
    }

    /**
     * 新增标签
     *
     * @param prodTag 实体对象
     * @return insert
     */
    @Override
    @CacheEvict(key = TagConstant.TAG_LIST)
    public boolean save(ProdTag prodTag) {
        prodTag.setProdCount(0L);
        prodTag.setShopId(1L);
        prodTag.setIsDefault(1);
        prodTag.setCreateTime(new Date());
        prodTag.setUpdateTime(new Date());
        return prodTagMapper.insert(prodTag) > 0;
    }

    /**
     * 修改标签
     *
     * @param prodTag 实体对象
     * @return update
     */
    @Override
    @CacheEvict(key = TagConstant.TAG_LIST)
    public boolean updateById(ProdTag prodTag) {
        prodTag.setUpdateTime(new Date());
        return prodTagMapper.updateById(prodTag) > 0;
    }

    /**
     * 删除标签
     *
     * @param id 主键ID
     * @return delete
     */
    @Override
    @CacheEvict(key = TagConstant.TAG_LIST)
    public boolean removeById(Serializable id) {
        return prodTagMapper.deleteById(id) > 0;
    }
}
