package com.powernode.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.powernode.domain.SysUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author wangjunchen
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
    Long selectIdByName(String username);
}