package com.chundengtai.base.server.dao;


import com.chundengtai.base.server.common.BaseBizMapper;
import com.chundengtai.base.server.entity.SmsLogVo;
import com.chundengtai.base.server.entity.UserVo;
import org.apache.ibatis.annotations.Param;

/**
 * 用户
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-03-23 15:22:06
 */
public interface ApiUserMapper extends BaseBizMapper<UserVo> {
    UserVo queryByMobile(String mobile);

    UserVo queryByOpenId(@Param("openId") String openId);

    /**
     * 获取用户最后一条短信
     *
     * @param user_id
     * @return
     */
    SmsLogVo querySmsCodeByUserId(@Param("user_id") Long user_id);

    /**
     * 保存短信
     *
     * @param smsLogVo
     * @return
     */
    int saveSmsCodeLog(SmsLogVo smsLogVo);
    
    /**
     * 更新分销比例
     * @param vo
     * @return
     */
    int updatefx(UserVo vo);
    
}

