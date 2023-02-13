package com.powernode.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.powernode.domain.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author wangjunchen
 */
public interface SysRoleService extends IService<SysRole> {

    /**
     * 多条件分页查询角色列表
     *
     * @param page 分页
     * @param sysRole 角色
     * @return page分页
     */
    Page<SysRole> selectSysRolePage(Page<SysRole> page, SysRole sysRole);
}
