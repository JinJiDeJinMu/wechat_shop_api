package com.chundengtai.base.dao;

import com.chundengtai.base.common.BaseBizMapper;
import com.chundengtai.base.entity.UserEntity;

/**
 * 会员Dao
 */
public interface UserDao extends BaseBizMapper<UserEntity> {

    void updatePromoter(UserEntity user);
}
