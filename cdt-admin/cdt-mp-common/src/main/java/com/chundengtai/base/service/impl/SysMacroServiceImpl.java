package com.chundengtai.base.service.impl;

import com.chundengtai.base.dao.SysMacroDao;
import com.chundengtai.base.entity.SysMacroEntity;
import com.chundengtai.base.service.SysMacroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 通用字典表实现类
 */
@Service("sysMacroService")
public class SysMacroServiceImpl implements SysMacroService {
    @Autowired
    private SysMacroDao sysMacroDao;

    @Override
    public SysMacroEntity queryObject(Long macroId) {
        return sysMacroDao.queryObject(macroId);
    }

    @Override
    public List<SysMacroEntity> queryList(Map<String, Object> map) {
        return sysMacroDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return sysMacroDao.queryTotal(map);
    }

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public int save(SysMacroEntity sysMacro) {
        sysMacro.setGmtCreate(new Date());
        sysMacroDao.save(sysMacro);
        redisTemplate.opsForValue().set("macroList", queryList(new HashMap<>()), 5, TimeUnit.DAYS);
        return 1;
    }

    @Override
    public int update(SysMacroEntity sysMacro) {
        sysMacro.setGmtModified(new Date());
        sysMacroDao.update(sysMacro);
        redisTemplate.opsForValue().set("macroList", queryList(new HashMap<>()), 5, TimeUnit.DAYS);
        return 1;
    }

    @Override
    public int delete(Long macroId) {
        sysMacroDao.delete(macroId);
        redisTemplate.opsForValue().set("macroList", queryList(new HashMap<>()), 5, TimeUnit.DAYS);
        return 1;
    }

    @Override
    public int deleteBatch(Long[] macroIds) {
        sysMacroDao.deleteBatch(macroIds);
        redisTemplate.opsForValue().set("macroList", queryList(new HashMap<>()), 5, TimeUnit.DAYS);
        return 1;
    }

    @Override
    public List<SysMacroEntity> queryMacrosByValue(String value) {
        return sysMacroDao.queryMacrosByValue(value);
    }
}
