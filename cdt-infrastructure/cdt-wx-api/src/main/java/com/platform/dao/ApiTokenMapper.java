package com.platform.dao;

import com.chundengtai.base.common.BaseBizMapper;
import com.platform.entity.TokenEntity;
import org.apache.ibatis.annotations.Param;

/**
 * 用户Token
 *
 * @date 2017-03-23 15:22:07
 */
public interface ApiTokenMapper extends BaseBizMapper<TokenEntity> {

    TokenEntity queryByUserId(@Param("userId") Long userId);

    TokenEntity queryByToken(@Param("token") String token);

}
