package com.platform.dao;

import com.platform.common.BaseBizMapper;
import com.platform.entity.UserEntity;

/**
 * 会员Dao
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-08-16 15:02:28
 */
public interface UserDao extends BaseBizMapper<UserEntity> {

    void updatePromoter(UserEntity user);
}
