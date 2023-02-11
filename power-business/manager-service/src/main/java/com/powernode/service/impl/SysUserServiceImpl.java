package com.powernode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.powernode.domain.SysUser;
import com.powernode.mapper.SysUserMapper;
import com.powernode.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author wangjunchen
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public Page<SysUser> selectSysUserPage(Page<SysUser> page, SysUser sysUser) {
        // 用户输入的数据基本都使用like关键字模糊查询
        // username like '%'#{sysUser.username}'%'
        return sysUserMapper
                .selectPage(page, new LambdaQueryWrapper<SysUser>()
                        .like(StringUtils.hasText(sysUser.getUsername()), SysUser::getUsername, sysUser.getUsername())
                        .orderByDesc(SysUser::getCreateTime));
        //<if test="username != null and username != ''">
        // username like '%'#{sysUser.username}'%'
        // </if>
    }
}
