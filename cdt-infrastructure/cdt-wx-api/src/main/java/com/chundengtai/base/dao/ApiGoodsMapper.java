package com.chundengtai.base.dao;

import com.chundengtai.base.common.BaseBizMapper;
import com.chundengtai.base.entity.GoodsVo;
import com.chundengtai.base.utils.Query;

import java.util.List;
import java.util.Map;

/**
 * @date 2017-08-11 09:16:45
 */
public interface ApiGoodsMapper extends BaseBizMapper<GoodsVo> {

    Integer checkBuy(Object id);

    List<GoodsVo> queryHotGoodsList(Map<String, Object> params);

    List<GoodsVo> queryCatalogProductList(Map<String, Object> params);

    List<GoodsVo> queryKillList();

    Integer queryMaxId();

    List<GoodsVo> queryTop4(Integer brand_id);

    List<GoodsVo> queryFxList(Map<String, Object> map);

    int queryFxTotal(Query query);

    List<GoodsVo> queryGroupList(Query query);

    int updateBrowse(GoodsVo goods);

    int queryGroupTotal(Query query);

    int queryKillTotal(Query query);

    List<GoodsVo> queryKillPage(Query query);


}
