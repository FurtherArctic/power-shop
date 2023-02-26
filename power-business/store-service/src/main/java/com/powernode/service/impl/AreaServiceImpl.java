package com.powernode.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.powernode.domain.Area;
import com.powernode.mapper.AreaMapper;
import com.powernode.service.AreaService;
import org.springframework.stereotype.Service;
/**
 * @author wangjunchen
 */
@Service
public class AreaServiceImpl extends ServiceImpl<AreaMapper, Area> implements AreaService{
}
