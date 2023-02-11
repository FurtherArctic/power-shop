package com.powernode.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.powernode.constant.MenuConstant;
import com.powernode.domain.SysMenu;
import com.powernode.mapper.SysMenuMapper;
import com.powernode.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wangjunchen
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Autowired
    private SysMenuMapper sysMenuMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public List<SysMenu> selectUserMenuList(String userId) {

        //先从redis缓存中获取用户的菜单集合
        String menuJsonStr = stringRedisTemplate.opsForValue().get(MenuConstant.MENU_PREFIX + userId);
        //判断是否有值
        List<SysMenu> sysMenuList = null;
        if (StringUtils.hasText(menuJsonStr)) {
            //有：直接返回
            //将json格式的字符串转换为List集合
            sysMenuList = JSON.parseArray(menuJsonStr, SysMenu.class);
        } else {
            //没有，去数据库中查询数据并存储到redis中
            sysMenuList = sysMenuMapper.selectUserMenuList(userId);
            //将菜单集合转换为json字符串存放到redis缓存中
            stringRedisTemplate.opsForValue().set(MenuConstant.MENU_PREFIX + userId, JSON.toJSONString(sysMenuList));
        }
        //将菜单集合转换为树结构的菜单集合
        return transformTree(sysMenuList, 0L);
    }

    /**
     * 集合转换为树结构，分两种情况考虑，一种是已知菜单树深度2级，使用循环
     * 另一种是未知菜单深度，大于2级，使用递归
     *
     * @param sysMenuList sysMenuList
     * @param pid         pid
     * @return root
     */
    private List<SysMenu> transformTree(List<SysMenu> sysMenuList, long pid) {

        /////////////////////////////已知菜单深度///////////////////////////
        //获取根节点
        /*
        List<SysMenu> root = sysMenuList.stream().filter(sysMenu -> sysMenu.getParentId().equals(pid)).collect(Collectors.toList());
        //循环根节点
        root.forEach(r -> {
            List<SysMenu> child = sysMenuList.stream().filter(sysMenu1 -> sysMenu1.getParentId().equals(r.getMenuId())).collect(Collectors.toList());
            //将子目录存放到当前根目录中
            r.setList(child);
        });
         */
        /////////////////////////////已知菜单深度///////////////////////////


        /////////////////////////////未知菜单深度///////////////////////////
        List<SysMenu> root = sysMenuList.stream().filter(sysMenu -> sysMenu.getParentId().equals(pid)).collect(Collectors.toList());

        root.forEach(r -> r.setList(transformTree(sysMenuList, r.getMenuId())));
        return root;
    }


}
