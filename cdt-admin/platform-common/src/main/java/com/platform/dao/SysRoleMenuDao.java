package com.platform.dao;

import com.platform.common.BaseBizMapper;
import com.platform.entity.SysRoleMenuEntity;

import java.util.List;

/**
 * 角色与菜单对应关系
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2016年9月18日 上午9:33:46
 */
public interface SysRoleMenuDao extends BaseBizMapper<SysRoleMenuEntity> {

    /**
     * 根据角色ID，获取菜单ID列表
     */
    List<Long> queryMenuIdList(Long roleId);
}
