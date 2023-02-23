package com.powernode.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.powernode.constant.ProdSpecConstant;
import com.powernode.domain.ProdProp;
import com.powernode.domain.ProdPropValue;
import com.powernode.mapper.ProdPropMapper;
import com.powernode.mapper.ProdPropValueMapper;
import com.powernode.service.ProdPropService;
import com.powernode.service.ProdPropValueService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wangjunchen
 */
@Service
@CacheConfig(cacheNames = "com.powernode.service.impl.ProdPropServiceImpl")
public class ProdPropServiceImpl extends ServiceImpl<ProdPropMapper, ProdProp> implements ProdPropService {

    @Resource
    private ProdPropMapper prodPropMapper;
    @Resource
    private ProdPropValueMapper prodPropValueMapper;

    @Resource
    private ProdPropValueService prodPropValueService;

    @Override
    public Page<ProdProp> selectProdSpecPage(Page<ProdProp> page, ProdProp prodProp) {
        //分页查询商品规格列表
        page = prodPropMapper.selectPage(page, new LambdaQueryWrapper<ProdProp>()
                .like(StringUtils.hasText(prodProp.getPropName()), ProdProp::getPropName, prodProp.getPropName())
        );
        //获取商品规格记录
        List<ProdProp> prodPropList = page.getRecords();
        //判断是否有值
        if (CollectionUtil.isEmpty(prodPropList) || prodPropList.size() == 0) {
            return page;
        }
        //从商品属性集合中获取商品属性id集合
        List<Long> propIdList = prodPropList.stream().map(ProdProp::getPropId).collect(Collectors.toList());
        //根据商品属性id集合查询商品属性值集合
        List<ProdPropValue> prodPropValueList = prodPropValueMapper.selectList(new LambdaQueryWrapper<ProdPropValue>()
                .in(ProdPropValue::getPropId, propIdList)
        );
        //循环遍历商品属性集合,不要在循环里面写sql语句
        prodPropList.forEach(prodProp1 -> {
            //从商品属性值集合中过滤出与当前商品属性id一致的商品属性值集合
            List<ProdPropValue> prodPropValues = prodPropValueList.stream()
                    .filter(prodPropValue -> prodPropValue.getPropId().equals(prodProp1.getPropId()))
                    .collect(Collectors.toList());
            //组装数据
            prodProp1.setProdPropValues(prodPropValues);
        });
        return page;
    }

    @Override
    @CacheEvict(key = ProdSpecConstant.PROD_PROP_LIST)
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean save(ProdProp prodProp) {
        //新增商品属性
        prodProp.setRule(2);
        prodProp.setShopId(1L);

        int i = prodPropMapper.insert(prodProp);
        if (i > 0) {
            //新增商品属性值
            //获取商品属性值集合对象
            List<ProdPropValue> prodPropValueList = prodProp.getProdPropValues();
            //判断属性值是否有值
            if (CollectionUtil.isEmpty(prodPropValueList) || prodPropValueList.size() == 0) {
                throw new RuntimeException("商品属性值不能为空");
            }
            Long propId = prodPropMapper.selectOne(new QueryWrapper<ProdProp>().eq("prop_name", prodProp.getPropName())).getPropId();
            //有值就循环遍历，给每一个属性值对象添加属性id
            prodPropValueList.forEach(prodPropValue -> prodPropValue.setPropId(propId));
            //批量新增商品属性值集合
            prodPropValueService.saveBatch(prodPropValueList);
        }
        return i > 0;
    }
}
