package com.powernode.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.powernode.domain.IndexImg;
import com.powernode.service.IndexImgService;
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
@Api(tags = "轮播图接口管理")
@RequestMapping("admin/indexImg")
@RestController
public class IndexImgController {
    @Resource
    private IndexImgService indexImgService;

    @ApiOperation("多条件查询轮播图列表")
    @GetMapping("page")
    @PreAuthorize("hasAuthority('admin:indexImg:page')")
    public ResponseEntity<Page<IndexImg>> loadIndexImgPage(Page<IndexImg> page, IndexImg indexImg) {
        page = indexImgService.selectIndexImgPage(page, indexImg);
        return ResponseEntity.ok(page);
    }
}
