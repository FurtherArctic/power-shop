package com.powernode.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.powernode.domain.ProdProp;
import com.powernode.domain.ProdPropValue;
import com.powernode.service.ProdPropService;
import com.powernode.service.ProdPropValueService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wangjunchen
 */
@Api(tags = "商品规格接口管理")
@RestController
@RequestMapping("/prod/spec")
public class ProdSpecController {
    @Resource
    private ProdPropService prodPropService;

    @Autowired
    private ProdPropValueService prodPropValueService;

    @ApiOperation("多条件分页查询商品规格列表")
    @GetMapping("page")
    @PreAuthorize("hasAuthority('prod:spec:page')")
    public ResponseEntity<Page<ProdProp>> loadProdSpecPage(Page<ProdProp> page, ProdProp prodProp) {
        page = prodPropService.selectProdSpecPage(page, prodProp);
        return ResponseEntity.ok(page);
    }

    /**
     * 新增商品规格，需要操作两张表，属性表中添加一条记录，属性值表中添加多条记录，因为一个属性可以对应多个属性值
     *
     * @param prodProp 商品属性
     * @return responseEntity
     */
    @ApiOperation("新增商品规格")
    @PostMapping
    @PreAuthorize("hasAuthority('prod:spec:save')")
    public ResponseEntity<Void> saveProdSpec(@RequestBody ProdProp prodProp) {
        prodPropService.save(prodProp);
        return ResponseEntity.ok().build();
    }

    /**
     * 修改商品规格，通常情况下每个属性对应多个属性值，如果只更改其中某几个属性值操作会过于繁琐
     * 因此，可以先将其中原有的属性值全部删除，然后将修改后的属性值重新添加
     *
     * @param prodProp 商品属性
     * @return ResponseEntity
     */
    @ApiOperation("修改商品规格")
    @PutMapping
    @PreAuthorize("hasAuthority('prod:spec:update')")
    public ResponseEntity<Void> updateProdSpec(@RequestBody ProdProp prodProp) {
        prodPropService.updateById(prodProp);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("删除商品规格")
    @DeleteMapping("{propId}")
    @PreAuthorize("hasAuthority('prod:spec:delete')")
    public ResponseEntity<Void> deleteProdSpec(@PathVariable Long propId) {
        prodPropService.removeById(propId);
        return ResponseEntity.ok().build();
    }

    /**
     * 查询商品规格列表，包括商品的属性和对应的属性值
     * 属性数量可以有很多，且不会频繁更改，因此要用到缓存
     * 属性值通常比较少，且改动频繁，不需要添加到缓存
     *
     * @return prodPropList
     */
    @ApiOperation("查询商品规格属性集合")
    @GetMapping("list")
    @PreAuthorize("hasAuthority('prod:spec:page')")
    public ResponseEntity<List<ProdProp>> loadProdPropList() {
        List<ProdProp> prodPropList = prodPropService.list();
        return ResponseEntity.ok(prodPropList);
    }

    /**
     * 根据属性标识查询属性值集合，不需要缓存，因此不用重写，直接在这里查询即可
     *
     * @param propId 属性标识id
     * @return 属性值集合
     */
    @ApiOperation("根据属性标识查询属性值集合")
    @GetMapping("listSpecValue/{propId}")
    @PreAuthorize("hasAuthority('prod:spec:page')")
    public ResponseEntity<List<ProdPropValue>> loadProdPropValueList(@PathVariable Long propId) {
        List<ProdPropValue> prodPropValueList = prodPropValueService.list(new LambdaQueryWrapper<ProdPropValue>()
                .eq(ProdPropValue::getPropId, propId)
        );
        return ResponseEntity.ok(prodPropValueList);
    }
}
