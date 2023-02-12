package com.powernode.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.powernode.domain.SysUser;
import com.powernode.service.SysUserService;
import com.powernode.utils.AuthUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author wangjunchen
 */
@Api(tags = "后台管理接口管理")
@RequestMapping("sys/user")
@RestController
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @ApiOperation("查询登录用户信息")
    @GetMapping("info")
    public ResponseEntity<SysUser> loadUserInfo() {
        //获取登录用户id
        String userId = AuthUtil.getLoginUserId();
        //根据用户id获取用户信息
        SysUser sysUser = sysUserService.getById(userId);
        return ResponseEntity.ok(sysUser);
    }

    /**
     * sys/user/page
     *
     * @return
     */
    @ApiOperation("多条件分页查询管理员列表")
    @GetMapping("page")
    @PreAuthorize("hasAuthority('sys:user:page')")
    public ResponseEntity<Page<SysUser>> loadSysUserPage(Page<SysUser> page, SysUser sysUser) {

        page = sysUserService.selectSysUserPage(page, sysUser);
        return ResponseEntity.ok(page);
    }

    @ApiOperation("新增管理员")
    @PostMapping
    @PreAuthorize("hasAuthority('sys:user:save')")
    public ResponseEntity<Void> saveSysUser(@RequestBody SysUser sysUser) {
        sysUserService.save(sysUser);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("查询管理员详情")
    @GetMapping("info/{id}")
    @PreAuthorize("hasAuthority('sys:user:info')")
    public ResponseEntity<SysUser> loadSysUserInfo(@PathVariable Long id) {
        SysUser sysUser = sysUserService.getById(id);
        return ResponseEntity.ok(sysUser);
    }

    @ApiOperation("修改管理员信息")
    @PutMapping
    @PreAuthorize("hasAuthority('sys:user:update')")
    public ResponseEntity<Void> updateSysUser(@RequestBody SysUser sysUser) {
        sysUserService.updateById(sysUser);
        return ResponseEntity.ok().build();
    }

    /**
     * 批量删除管理员用户，此种方法只能够删除sys_user表中的用户信息
     * 但sys_user_role表中的用户id对应的角色信息不能删除
     *
     * @param ids 用户id集合
     * @return 响应
     */
    @ApiOperation("批量删除管理员")
    @DeleteMapping("{ids}")
    @PreAuthorize("hasAuthority('sys:user:delete')")
    public ResponseEntity<Void> deleteSysUser(@PathVariable List<Long> ids) {
        sysUserService.removeByIds(ids);

        return ResponseEntity.ok().build();
    }
}
