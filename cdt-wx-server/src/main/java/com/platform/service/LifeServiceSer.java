package com.platform.service;

import com.chundengtai.base.constant.CacheConstant;
import com.platform.util.JuheUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


/**
 * 生活服务查询
 */
@Service
@Slf4j
public class LifeServiceSer {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 身份验证
     * @param idcard
     * @param realname
     * @return
     */
    public Map<String, Object> idcardCheck(String idcard, String realname, Long userId) {
        Map<String, Object> resultObj = new HashMap<String, Object>();

        int count = 0;
        boolean flag = redisTemplate.hasKey(CacheConstant.SYS_ID_CARD_CHECL + userId);
        if (flag == true) {
            //System.out.println(RedisUtils.get(J2CacheUtils.SYS_ID_CARD_CHECL + userId));
            count = Integer.parseInt(redisTemplate.opsForValue().get(CacheConstant.SYS_ID_CARD_CHECL + userId).toString());
            if (count == 3) {
                resultObj.put("errno", 3);
                resultObj.put("errmsg", "同一用户一分钟只可以认证三次。");
                return resultObj;
            } else {
                count++;
                log.info("不够三次增加次数" + count);
                redisTemplate.delete(CacheConstant.SYS_ID_CARD_CHECL + userId);
                redisTemplate.opsForValue().set(CacheConstant.SYS_ID_CARD_CHECL + userId, String.valueOf(count), 60);
            }
        } else {
            log.info("第一次设置次数60秒");
            redisTemplate.opsForValue().set(CacheConstant.SYS_ID_CARD_CHECL + userId, String.valueOf(++count), 60);
        }
//    	if(1==1) {
//        	return resultObj;
//    	}

        if (org.apache.commons.lang.StringUtils.isBlank(idcard)
                || org.apache.commons.lang.StringUtils.isBlank(realname)) {
            resultObj.put("errno", 2);
            resultObj.put("errmsg", "姓名、身份证号必须填写");
            return resultObj;
        }

        //查询违章接口次数
        String url = "http://op.juhe.cn/idcard/query";//请求接口地址
        String key = "f5b15b0bb9e5b81db6c3d00822c6f327";//企业申请的key
        Map<String, String> params = new HashMap<String, String>();//请求参数
        params.put("key", key);
        params.put("idcard", idcard);
        params.put("realname", realname);
        String result = null;
        try {
            result = JuheUtil.net(url, params, "GET");
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject object = JSONObject.fromObject(result);
        if (object.getInt("error_code") == 0) {
            JSONObject resultObject = JSONObject.fromObject(object.get("result"));
            String res = resultObject.get("res").toString().toString();
            if ("1".equals(res)) {
                resultObj.put("errno", 0);
                resultObj.put("errmsg", "匹配成功");
                return resultObj;
            } else {
                resultObj.put("errno", 1);
                resultObj.put("errmsg", "匹配失败");
                return resultObj;
            }
        } else {
            resultObj.put("errno", object.get("error_code").toString());
            resultObj.put("errmsg", object.get("reason").toString());
            return resultObj;
        }
    }
}
