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
     * @return responseEntity
     */
    @ApiOperation("新增商品规格")
    @PostMapping
    @PreAuthorize("hasAuthority('prod:spec:save')")
    public ResponseEntity<Void> saveProdSpec(@RequestBody ProdProp prodProp) {
        prodPropService.save(prodProp);
        return ResponseEntity.ok().build();
    }
}
