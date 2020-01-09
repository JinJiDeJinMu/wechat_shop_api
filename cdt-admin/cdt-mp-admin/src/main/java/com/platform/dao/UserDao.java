package com.platform.dao;

import com.chundengtai.base.common.BaseBizMapper;
import com.platform.entity.UserEntity;

/**
 * 会员Dao
 *
 * @date 2017-08-16 15:02:28
 */
public interface UserDao extends BaseBizMapper<UserEntity> {

    void updatePromoter(UserEntity user);
}
