package com.powernode.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.powernode.constant.IndexImgConstant;
import com.powernode.domain.IndexImg;
import com.powernode.domain.Prod;
import com.powernode.feign.IndexImgProdFeign;
import com.powernode.mapper.IndexImgMapper;
import com.powernode.service.IndexImgService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Date;

/**
 * @author wangjunchen
 */
@Service
@CacheConfig(cacheNames = "com.powernode.service.impl.IndexImgServiceImpl")
public class IndexImgServiceImpl extends ServiceImpl<IndexImgMapper, IndexImg> implements IndexImgService {

    @Resource
    private IndexImgMapper indexImgMapper;

    @Resource
    private IndexImgProdFeign indexImgProdFeign;

    @Override
    public Page<IndexImg> selectIndexImgPage(Page<IndexImg> page, IndexImg indexImg) {
        return indexImgMapper.selectPage(page, new LambdaQueryWrapper<IndexImg>()
                .eq(ObjectUtil.isNotEmpty(indexImg.getStatus()), IndexImg::getStatus, indexImg.getStatus())
                .orderByDesc(IndexImg::getSeq)
        );
    }

    @Override
    @CacheEvict(key = IndexImgConstant.FRONT_INDEX_IMG_LIST)
    public boolean save(IndexImg indexImg) {
        //判断类型是否为-1，若为-1则relation为-1
        if (indexImg.getType() == -1) {
            indexImg.setRelation(-1L);
        }
        //获取状态,状态为1 代表可用，添加上传时间，0表示禁用，不用上传
        if (1 == indexImg.getStatus()) {
            indexImg.setUploadTime(new Date());
        }
        return indexImgMapper.insert(indexImg) > 0;
    }

    @Override
    public IndexImg getById(Serializable id) {
        //根据标识查询轮播图信息
        IndexImg indexImg = indexImgMapper.selectById(id);
        //获取轮播图类型type,-1未关联，0关联
        Integer type = indexImg.getType();
        if (0 == type) {
            //获取商品信息，商品信息在product-service服务中，跨服务获取信息需要使用openFeign
            Long prodId = indexImg.getRelation();
            Prod prod = indexImgProdFeign.getProdById(prodId);
            //pic和prodName两个字段实体类中没有，需要手动添加
            indexImg.setPic(prod.getPic());
            indexImg.setProdName(prod.getProdName());
        }

        return indexImg;
    }
}
