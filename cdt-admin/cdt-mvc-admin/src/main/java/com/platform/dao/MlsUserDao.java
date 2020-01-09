package com.platform.dao;

import com.platform.common.BaseBizMapper;
import com.platform.entity.MlsUserEntity2;

/**
 * 会员Dao
 *
 * @date 2017-08-16 15:02:28
 */
public interface MlsUserDao extends BaseBizMapper<MlsUserEntity2> {

    int updatefx(MlsUserEntity2 user);
}