package com.powernode.controller;

import com.powernode.domain.Area;
import com.powernode.service.AreaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wangjunchen
 */
@Api(tags = "地址管理接口")
@RestController
@RequestMapping("admin/area")
public class AreaController {
    @Resource
    private AreaService areaService;

    @ApiOperation("查询地址列表")
    @GetMapping("list")
    @PreAuthorize("hasAuthority('admin:area:list')")
    public ResponseEntity<List<Area>> loadAreaList() {
        List<Area> areaList = areaService.list();
        return ResponseEntity.ok(areaList);
    }
}
