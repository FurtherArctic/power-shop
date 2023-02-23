package com.powernode.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.powernode.domain.ProdProp;
import com.powernode.service.ProdPropService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author wangjunchen
 */
@Api(tags = "商品规格接口管理")
@RestController
@RequestMapping("/prod/spec")
public class ProdSpecController {
    @Resource
    private ProdPropService prodPropService;

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
}
