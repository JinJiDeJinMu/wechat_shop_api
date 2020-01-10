package com.chundengtai.base.dao;

import com.chundengtai.base.common.BaseBizMapper;
import com.chundengtai.base.entity.MlsUserEntity2;

/**
 * 会员Dao
 *
 * @date 2017-08-16 15:02:28
 */
public interface MlsUserDao extends BaseBizMapper<MlsUserEntity2> {

    int updatefx(MlsUserEntity2 user);
}
