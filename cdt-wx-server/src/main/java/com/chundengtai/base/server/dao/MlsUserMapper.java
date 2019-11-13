package com.chundengtai.base.server.dao;

import com.chundengtai.base.server.entity.UserGoods;
import com.chundengtai.base.server.common.EntityDao;
import com.platform.entity.MlsUserEntity2;

import java.util.List;
import java.util.Map;

/**
 * 分销用户<br>
 */
public interface MlsUserMapper extends EntityDao<MlsUserEntity2,Long> {
	public MlsUserEntity2 findByDeviceId(String deviceId) ;
	public MlsUserEntity2 findByUserTel(String userTel) ;
	public MlsUserEntity2 findByUserId(Long userId) ;
	public MlsUserEntity2 findByMerchantId(Integer mid) ;
	public Map<String,Object> getSysUser(Long userid) ;
	public Map<String,Object> getSysUserByMid(Long mid) ;
	public void updateMoney(MlsUserEntity2 mlsUserVo) ;
	public void cancelMoney(MlsUserEntity2 mlsUserVo) ;
	public void updateRootId(MlsUserEntity2 mlsUserVo) ;
	public void updateTodaySales();
	public void updateGetProfit(MlsUserEntity2 mlsUserVo);
	public void updateFid(MlsUserEntity2 mlsUserVo);
	public void insertUserGoods(UserGoods userGoods);
	public List<UserGoods> getUserGoods(Long userid);
	public void tx(MlsUserEntity2 mlsUserVo);

    UserGoods findUserGoods(UserGoods userGoods);
    /**
     * 根据key，查询value
     */
    String queryByKey(String paramKey);
}
