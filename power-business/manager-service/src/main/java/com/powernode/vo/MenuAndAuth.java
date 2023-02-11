package com.powernode.vo;

import com.powernode.domain.SysMenu;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author wangjunchen
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuAndAuth {
    private List<String> authorities;
    private List<SysMenu> menuList;
}
