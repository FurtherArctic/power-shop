package com.powernode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.powernode.domain.SysUser;
import com.powernode.domain.SysUserRole;
import com.powernode.mapper.SysUserMapper;
import com.powernode.mapper.SysUserRoleMapper;
import com.powernode.service.SysUserRoleService;
import com.powernode.service.SysUserService;
import com.powernode.utils.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wangjunchen
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

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
    public boolean save(SysUser sysUser) {
        //新增管理员
        //密码加密
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        sysUser.setPassword(passwordEncoder.encode(sysUser.getPassword()));
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
                    // 由于id是数据库表中的自增字段
                    // 如果直接使用sysUser.getUserId()方法获取用户id，此时使用此方法获取到的是空值null
                    //因此使用根据用户名获取id
                    sysUserRole.setUserId(sysUserMapper.selectIdByName(sysUser.getUsername()));
                    sysUserRoleList.add(sysUserRole);
                });
                sysUserRoleService.saveBatch(sysUserRoleList);
            }
        }
        return i > 0;
    }

    /**
     * 点击编辑后查询管理员详情
     *
     * @param id 管理员id
     * @return 用户信息
     */
    @Override
    public SysUser getById(Serializable id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        //根据用户标识查询角色集合
        List<SysUserRole> sysUserRoleList = sysUserRoleMapper.selectList(new LambdaQueryWrapper<SysUserRole>()
                .eq(SysUserRole::getUserId, id)
        );
        //判断是否有值
        if (!CollectionUtils.isEmpty(sysUserRoleList) && sysUserRoleList.size() != 0) {
            //获取角色id集合
            List<Long> roleIdList = sysUserRoleList.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
            sysUser.setRoleIdList(roleIdList);
        }
        return sysUser;
    }

    /**
     * 修改管理员信息
     *
     * @param sysUser 实体对象
     * @return 修改结果，boolean型
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean updateById(SysUser sysUser) {
        //如果密码框中的有值，则修改密码，没有则不变
        String password = sysUser.getPassword();
        //判断是否有值
        if (StringUtils.hasText(password)) {
            //修改密码，密码加密
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            sysUser.setPassword(passwordEncoder.encode(password));
        }
        //删除管理员原有的角色
        sysUserRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>()
                .eq(SysUserRole::getUserId, sysUser.getUserId())
        );
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
        return sysUserMapper.updateById(sysUser) > 0;
    }
}
