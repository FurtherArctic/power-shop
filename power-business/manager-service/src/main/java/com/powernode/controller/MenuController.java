package com.powernode.controller;

import com.powernode.domain.SysMenu;
import com.powernode.service.SysMenuService;
import com.powernode.utils.AuthUtil;
import com.powernode.vo.MenuAndAuth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author wangjunchen
 */
@Api(tags = "菜单权限接口管理")
@RequestMapping("sys/menu")
@RestController
public class MenuController {

    @Autowired
    private SysMenuService sysMenuService;

    @ApiOperation("查询用户的菜单权限")
    @GetMapping("nav")
    public ResponseEntity<MenuAndAuth> loadUserMenuAndAuth() {
        MenuAndAuth menuAndAuth = new MenuAndAuth();
        //根据用户标识查询用户权限
        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        List<String> auths = authorities.stream().map(Objects::toString).collect(Collectors.toList());
        /*
        从security上下文中获取用户的id
        根据用户标识查询用户菜单集合
         */
        String userId = AuthUtil.getLoginUserId();
        List<SysMenu> sysMenuList = sysMenuService.selectUserMenuList(userId);

        menuAndAuth.setAuthorities(auths);
        menuAndAuth.setMenuList(sysMenuList);
        return ResponseEntity.ok(menuAndAuth);
    }
}
