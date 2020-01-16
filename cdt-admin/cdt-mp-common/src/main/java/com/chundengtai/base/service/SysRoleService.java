package com.chundengtai.base.service;

import com.chundengtai.base.entity.SysRoleEntity;
import com.chundengtai.base.entity.UserWindowDto;
import com.chundengtai.base.page.Page;

import java.util.List;
import java.util.Map;


/**
 * 角色
 */
public interface SysRoleService {

    SysRoleEntity queryObject(Long roleId);

    List<SysRoleEntity> queryList(Map<String, Object> map);

    int queryTotal(Map<String, Object> map);

    void save(SysRoleEntity role);

    void update(SysRoleEntity role);

    void deleteBatch(Long[] roleIds);

    /**
     * 查询用户创建的角色ID列表
     */
    List<Long> queryRoleIdList(Long createUserId);

    /**
     * 分页查询角色审批选择范围
     * @return
     */
    Page<UserWindowDto> queryPageByDto(UserWindowDto userWindowDto, int pageNmu);
}
