package com.chundengtai.base.service.admin.impl;

import com.chundengtai.base.dao.GoodsIssueDao;
import com.chundengtai.base.entity.GoodsIssueEntity;
import com.chundengtai.base.service.admin.GoodsIssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("goodsIssueService")
public class GoodsIssueServiceImpl implements GoodsIssueService {
    @Autowired
    private GoodsIssueDao goodsIssueDao;

    @Override
    public GoodsIssueEntity queryObject(Integer id) {
        return goodsIssueDao.queryObject(id);
    }

    @Override
    public List<GoodsIssueEntity> queryList(Map<String, Object> map) {
        return goodsIssueDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return goodsIssueDao.queryTotal(map);
    }

    @Override
    public int save(GoodsIssueEntity goodsIssue) {
        return goodsIssueDao.save(goodsIssue);
    }

    @Override
    public int update(GoodsIssueEntity goodsIssue) {
        return goodsIssueDao.update(goodsIssue);
    }

    @Override
    public int delete(Integer id) {
        return goodsIssueDao.delete(id);
    }

    @Override
    public int deleteBatch(Integer[] ids) {
        return goodsIssueDao.deleteBatch(ids);
    }
}
