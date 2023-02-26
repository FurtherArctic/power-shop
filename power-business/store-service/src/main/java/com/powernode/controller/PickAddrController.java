package com.powernode.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.powernode.domain.PickAddr;
import com.powernode.service.PickAddrService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author wangjunchen
 */
@Api(tags = "自提点接口管理")
@RestController
@RequestMapping("shop/pickAddr")
public class PickAddrController {
    @Resource
    private PickAddrService pickAddrService;

    /**
     * 分页查询，单表查询。
     *
     * @param page     分页
     * @param pickAddr 自提点
     * @return 分页数据
     */
    @ApiOperation("多条件分页查询自提点列表")
    @GetMapping("page")
    @PreAuthorize("hasAuthority('shop:pickAddr:page')")
    public ResponseEntity<Page<PickAddr>> loadPickAddrPage(Page<PickAddr> page, PickAddr pickAddr) {
        page = pickAddrService.selectPickAddrPage(page, pickAddr);
        return ResponseEntity.ok(page);
    }
}
