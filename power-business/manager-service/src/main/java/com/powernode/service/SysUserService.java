package com.powernode.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.powernode.domain.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;
    /**
 * @author wangjunchen
 */
public interface SysUserService extends IService<SysUser>{


        Page<SysUser> selectSysUserPage(Page<SysUser> page, SysUser sysUser);
    }
