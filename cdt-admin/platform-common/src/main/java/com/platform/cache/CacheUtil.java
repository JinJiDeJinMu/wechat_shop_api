package com.platform.cache;

import com.platform.dao.SysMacroDao;
import com.platform.entity.SysMacroEntity;
import com.platform.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class CacheUtil {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    SysMacroDao macroDao;

    @PostConstruct
    public void init() {
        if (null != macroDao) {
            redisTemplate.opsForValue().set("macroList", macroDao.queryList(new HashMap<String, Object>()), 5, TimeUnit.DAYS);
        }
    }

    /**
     * 根据字典标识获取字典中文
     *
     * @param preName 父级name
     * @param value   字典value
     * @return
     */
    public String getCodeName(String preName, String value) {
        String name = "";
        Long parentId = 0L;
        List<SysMacroEntity> sysMacroEntityList = (List<SysMacroEntity>) redisTemplate.opsForValue().get("macroList");

        if (!StringUtils.isNullOrEmpty(sysMacroEntityList)) {
            for (SysMacroEntity macroEntity : sysMacroEntityList) {
                if (preName.equals(macroEntity.getName())) {
                    parentId = macroEntity.getId();
                }
            }
            for (SysMacroEntity macroEntity : sysMacroEntityList) {
                if (value.equals(macroEntity.getValue()) && parentId.equals(macroEntity.getParentId())) {
                    name = macroEntity.getName();
                }
            }
        }
        return name;
    }

}