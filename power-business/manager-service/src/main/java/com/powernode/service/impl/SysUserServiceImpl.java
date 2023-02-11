package com.powernode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.powernode.domain.SysUser;
import com.powernode.domain.SysUserRole;
import com.powernode.mapper.SysUserMapper;
import com.powernode.service.SysUserRoleService;
import com.powernode.service.SysUserService;
import com.powernode.utils.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author wangjunchen
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Override
    public Page<SysUser> selectSysUserPage(Page<SysUser> page, SysUser sysUser) {
        // 用户输入的数据基本都使用like关键字模糊查询
        // username like '%'#{sysUser.username}'%'
        return sysUserMapper.selectPage(page, new LambdaQueryWrapper<SysUser>().like(StringUtils.hasText(sysUser.getUsername()), SysUser::getUsername, sysUser.getUsername()).orderByDesc(SysUser::getCreateTime));
        //<if test="username != null and username != ''">
        // username like '%'#{sysUser.username}'%'
        // </if>
    }

    /**
     * 新增管理员
     *
     * @param sysUser 管理员信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean save(SysUser sysUser) {
        //密码加密
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        sysUser.setPassword(passwordEncoder.encode(sysUser.getPassword()));
        //用户创建时间
        sysUser.setCreateTime(new Date());
        sysUser.setCreateUserId(Long.valueOf(AuthUtil.getLoginUserId()));
        int i = sysUserMapper.insert(sysUser);
        if (i > 0) {
            //获取用户角色id集合
            List<Long> roleIdList = sysUser.getRoleIdList();
            //判断是否有角色
            if (!CollectionUtils.isEmpty(roleIdList) && roleIdList.size() != 0) {
                //新增用户角色记录
                List<SysUserRole> sysUserRoleList = new ArrayList<>();
                //循环遍历用户角色id集合
                roleIdList.forEach(roleId -> {
                    SysUserRole sysUserRole = new SysUserRole();
                    sysUserRole.setRoleId(roleId);
                    sysUserRole.setUserId(sysUser.getUserId());
                    sysUserRoleList.add(sysUserRole);
                });
                sysUserRoleService.saveBatch(sysUserRoleList);
            }
        }
        return i > 0;
    }
}
