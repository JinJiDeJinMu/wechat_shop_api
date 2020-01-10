package com.platform.dao;

import com.chundengtai.base.common.BaseBizMapper;
import com.platform.entity.SysUserRoleEntity;

import java.util.List;

/**
 */
public interface SysUserRoleDao extends BaseBizMapper<SysUserRoleEntity> {

    /**
     * 根据用户ID，获取角色ID列表
     */
    List<Long> queryRoleIdList(Long userId);
}
