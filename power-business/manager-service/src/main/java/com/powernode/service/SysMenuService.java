package com.powernode.service;

import com.powernode.domain.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author wangjunchen
 */
public interface SysMenuService extends IService<SysMenu>{


        List<SysMenu> selectUserMenuList(String userId);
    }
