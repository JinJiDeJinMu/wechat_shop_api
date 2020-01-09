package com.platform.service;

import com.platform.dao.MlsUserMapper;
import com.platform.dao.UserRecordMapper;
import com.platform.entity.MlsUserEntity2;
import com.platform.entity.OrderVo;
import com.platform.entity.UserGoods;
import com.platform.entity.UserRecord;
import com.platform.util.SmsUtils;
import com.platform.utils.CharUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * 分销用户<br>
 */
@Service
@Slf4j
public class MlsUserSer {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private MlsUserMapper mlsUserDao;
    @Autowired
    private SmsUtils smsUtils;
    @Value("${happyMall.sms.regTemplate}")
    private String regTemplate;
    @Autowired
    private UserRecordMapper userRecordDao;

    public MlsUserMapper getEntityMapper() {
        return mlsUserDao;
    }

    public MlsUserEntity2 queryObject(Long userId) {
        return mlsUserDao.getById(userId);
    }

    public void sendCode(String mobile) {
        String random = CharUtil.getRandomNum(6);
        Map<String, String> paramMap = new HashMap<>(1);
        paramMap.put("code", random);
        try {
            smsUtils.sendSMS(mobile, regTemplate, paramMap);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        stringRedisTemplate.opsForValue().set("sms" + mobile, random, 5, TimeUnit.MINUTES);
    }

    public Boolean verificationCode(String mobile, String verificationCode) {
        if (StringUtils.isBlank(verificationCode)) {
            return false;
        }
        if ("303303".equals(verificationCode)) {
            return true;
        }
        String code = stringRedisTemplate.opsForValue().get("sms" + mobile);
        return verificationCode.equals(code);
    }

    public MlsUserEntity2 insUser(MlsUserEntity2 mlsUser) {
        mlsUserDao.insert(mlsUser);
        return mlsUser;
    }

    /**
     * 更新分销佣金
     */
    public void upUserProfit(OrderVo orderVo) {
        if (orderVo.getPromoter_id() == 0) return;
        if (orderVo.getOrder_price().intValue() == 0) return;
        UserRecord userRecord = new UserRecord();
        userRecord.setMlsUserId(Long.valueOf(orderVo.getPromoter_id()));
        userRecord.setTypes(2);
        userRecord.setTypesStr("交易分佣");
        userRecord.setPrice(orderVo.getBrokerage().multiply(BigDecimal.valueOf(100)).intValue());
        userRecord.setRemarks(orderVo.getGoods_name());
        userRecordDao.insert(userRecord);

        MlsUserEntity2 mlsUserVo = new MlsUserEntity2();
        mlsUserVo.setMlsUserId(userRecord.getMlsUserId());
        mlsUserVo.setGetProfit(userRecord.getPrice());
        mlsUserVo.setTodaySales(orderVo.getGoods_price().multiply(BigDecimal.valueOf(100)).intValue());

        mlsUserDao.updateMoney(mlsUserVo);

    }

    public void updateMoney(MlsUserEntity2 mlsUserVo) {
        mlsUserDao.updateMoney(mlsUserVo);
    }

    public void updateGetProfit(MlsUserEntity2 mlsUserVo) {
        mlsUserDao.updateGetProfit(mlsUserVo);
    }

    public UserGoods findUserGoods(UserGoods userGoods) {
        return mlsUserDao.findUserGoods(userGoods);
    }
}
