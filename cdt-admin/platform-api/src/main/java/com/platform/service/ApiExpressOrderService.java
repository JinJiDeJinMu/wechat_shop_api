package com.platform.service;


import com.platform.dao.ApiExpressOrderMapper;
import com.platform.entity.ExpressOrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;



/**
 * Service接口
 *
 * @date 2019-11-29 10:44:41
 */
@Service
public class ApiExpressOrderService {
    @Autowired
            private ApiExpressOrderMapper apiExpressOrderMapper;

    /**
     * 根据主键查询实体
     *
     * @param id 主键
     * @return 实体
     */
   public ExpressOrderVo queryObject(Integer id){
        return apiExpressOrderMapper.queryObject(id);
    }

    /**
     * 分页查询
     *
     * @param map 参数
     * @return list
     */
   public List<ExpressOrderVo> queryList(Map<String, Object> map){
       return apiExpressOrderMapper.queryList(map);
    }

    /**
     * 分页统计总数
     *
     * @param map 参数
     * @return 总数
     */
   public int queryTotal(Map<String, Object> map){
        return  apiExpressOrderMapper.queryTotal(map);
    }

    /**
     * 保存实体
     *
     * @param expressOrderVo 实体
     * @return 保存条数
     */
   public int save(ExpressOrderVo expressOrderVo){
        return apiExpressOrderMapper.save(expressOrderVo);
    }

    /**
     * 根据主键更新实体
     *
     * @param expressOrderVo 实体
     * @return 更新条数
     */
   public int update(ExpressOrderVo expressOrderVo){
        return apiExpressOrderMapper.update(expressOrderVo);
    }

    /**
     * 根据主键删除
     *
     * @param id
     * @return 删除条数
     */
   public int delete(Integer id){
        return apiExpressOrderMapper.delete(id);
    }

    /**
     * 根据主键批量删除
     *
     * @param ids
     * @return 删除条数
     */
   public int deleteBatch(Integer[] ids){
        return apiExpressOrderMapper.deleteBatch(ids);
    }
}
