package com.platform.dao;

import com.chundengtai.base.common.BaseBizMapper;
import com.platform.entity.MlsUserEntity2;
import com.platform.entity.SysUserEntity;
import com.platform.entity.UserWindowDto;

import java.util.List;
import java.util.Map;

/**
 * 系统用户
 */
public interface SysUserDao extends BaseBizMapper<SysUserEntity> {

    /**
     * 查询用户的所有权限
     *
     * @param userId 用户ID
     */
    List<String> queryAllPerms(Long userId);

    /**
     * 查询用户的所有菜单ID
     */
    List<Long> queryAllMenuId(Long userId);

    /**
     * 根据用户名，查询系统用户
     */
    SysUserEntity queryByUserName(String username);

    /**
     * 修改密码
     */
    int updatePassword(Map<String, Object> map);
    /**
     * 根据实体类查询
     * @param userWindowDto
     * @return
     */
    List<UserWindowDto> queryListByBean(UserWindowDto userWindowDto);
    /**
       *更新商户ID
     */
    int updateMerchantId(SysUserEntity t);
    
    int mlsUseCount(String tel);
    void insertMlsUse(MlsUserEntity2 t);
    void updateMlsUse(MlsUserEntity2 t);
}

