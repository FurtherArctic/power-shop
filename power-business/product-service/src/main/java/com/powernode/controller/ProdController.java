package com.powernode.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.powernode.domain.Prod;
import com.powernode.service.ProdService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wangjunchen
 */
@Api(tags = "商品接口管理")
@RestController
@RequestMapping("prod/prod")
public class ProdController {
    @Resource
    private ProdService prodService;

    @ApiOperation("多条件分页查询商品列表")
    @GetMapping("page")
    @PreAuthorize("hasAuthority('prod:prod:page')")
    public ResponseEntity<Page<Prod>> loadProdPage(Page<Prod> page, Prod prod) {
        page = prodService.selectProdPage(page, prod);
        return ResponseEntity.ok(page);
    }

    /**
     * 新增商品，产品属于SPU，一个SPU对应多个SKU，SKU是组成商品最小不可分割的单元
     * 新增商品时要求：
     * 1.要确定商品属于哪个类目（产品分类）
     * 2.确定商品是否要添加到某个或某几个商品标签中（商品标签分组）
     * 3.添加SKU（商品规格）
     * 分组表和商品表是多对多关系，需要拿到分组ID，放到分组标签表和商品信息表的维护表prod_tag_reference中
     * 一个商品有多个SKU，因此sku表中要有商品和属性的一对多关系
     * 因此，需要对商品对象类Prod添加一些属性：商品SKU集合skuList，商品标签列表集合tagList，配送方式内部类DeliveryModeVo对象
     *
     * @param prod 商品信息
     * @return responseEntity
     */
    @ApiOperation("新增商品")
    @PostMapping
    @PreAuthorize("hasAuthority('prod:prod:save')")
    public ResponseEntity<Void> saveProd(@RequestBody Prod prod) {
        prodService.save(prod);
        return ResponseEntity.ok().build();
    }

    /**
     * 根据标识查询商品详情，需要查询商品分组标签的集合以及sku集合
     *
     * @param prodId 商品id
     * @return responseEntity
     */
    @ApiOperation("根据标识查询商品详情")
    @GetMapping("info/{prodId}")
    @PreAuthorize("hasAuthority('prod:prod:info')")
    public ResponseEntity<Prod> loadProdInfo(@PathVariable Long prodId) {
        Prod prod = prodService.getById(prodId);
        return ResponseEntity.ok(prod);
    }

    /**
     * 修改商品信息，先删除所有属性，然后重新添加
     *
     * @param prod 商品信息
     * @return responseEntity
     */
    @ApiOperation("修改商品信息")
    @PutMapping
    @PreAuthorize("hasAuthority('prod:prod:update')")
    public ResponseEntity<Void> load(@RequestBody Prod prod) {
        prodService.updateById(prod);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("批量删除商品")
    @DeleteMapping("{prodIds}")
    @PreAuthorize("hasAuthority('prod:prod:delete')")
    public ResponseEntity<Void> deleteProd(@PathVariable List<Long> prodIds) {
        prodService.removeByIds(prodIds);
        return ResponseEntity.ok().build();
    }
}
