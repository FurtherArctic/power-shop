package com.powernode.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.powernode.domain.SysUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author wangjunchen
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
    /**
     * 根据用户名查询用户id
     *
     * @param username 用户名
     * @return 用户id
     */
    Long selectIdByName(String username);
}