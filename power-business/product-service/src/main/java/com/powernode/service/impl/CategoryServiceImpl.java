package com.powernode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.powernode.constant.CategoryConstant;
import com.powernode.domain.Category;
import com.powernode.mapper.CategoryMapper;
import com.powernode.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author wangjunchen
 */
@Service
@Slf4j
@CacheConfig(cacheNames = "com.powernode.service.impl.CategoryServiceImpl")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 获取所有分类
     *
     * @return Category集合
     */
    @Override
    @Cacheable(key = CategoryConstant.CATEGORY_LIST)
    public List<Category> list() {
        return categoryMapper.selectList(
                new LambdaQueryWrapper<Category>().orderByDesc(Category::getSeq)
        );
    }

    @Override
    @Cacheable(key = CategoryConstant.CATEGORY_LIST)
    public boolean save(Category category) {
        //获取parent_id
        Long parentId = category.getParentId();
        //判断是否为一级类目
        if (parentId == 0) {
            category.setGrade(1);
        } else {
            category.setGrade(2);
        }
        category.setRecTime(new Date());
        category.setUpdateTime(new Date());
        category.setShopId(1L);
        return categoryMapper.insert(category) > 0;
    }
}