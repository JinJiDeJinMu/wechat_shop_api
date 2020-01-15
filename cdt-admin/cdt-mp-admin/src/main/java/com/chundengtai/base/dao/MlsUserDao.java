package com.chundengtai.base.dao;

import com.chundengtai.base.common.BaseBizMapper;
import com.chundengtai.base.entity.MlsUserEntity2;

/**
 * 会员Dao
 */
public interface MlsUserDao extends BaseBizMapper<MlsUserEntity2> {

    int updatefx(MlsUserEntity2 user);
}
