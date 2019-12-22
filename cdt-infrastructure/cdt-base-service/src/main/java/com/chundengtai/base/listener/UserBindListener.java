package com.chundengtai.base.listener;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.chundengtai.base.bean.CdtDistributionLevel;
import com.chundengtai.base.bean.User;
import com.chundengtai.base.event.DistributionEvent;
import com.chundengtai.base.service.CdtDistributionLevelService;
import com.chundengtai.base.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component("UserBindListener")
@Slf4j
public class UserBindListener {
    @Autowired
    CdtDistributionLevelService service;

    @Autowired
    UserService userService;

    @EventListener(DistributionEvent.class)
    @Order(0)
    public void defaultShopCartEvent(DistributionEvent event) {
        log.info("----------用户绑定时间出发---------" + JSON.toJSONString(event));
        //todo:对推荐信息解密
        if (StringUtils.isEmpty(event.getEncryptCode())) {
            return;
        }

        if (event.getEncryptCode().contains("_")) {
            String promotionId = event.getEncryptCode().split("_")[0];
            Long parentId = Long.valueOf(promotionId);

            if (event.getUserId().equals(parentId)) {
                log.warn("自己不能推荐自己");
                return;
            }

            CdtDistributionLevel model = new CdtDistributionLevel();
            model.setUserId(event.getUserId());
            model.setParentId(parentId);
            model.setSponsorId(parentId);
            boolean result = service.save(model);

            //绑定user表层架关系

            LambdaUpdateWrapper<User> condition = new LambdaUpdateWrapper<>();
            condition.set(User::getFirstLeader, parentId);
            condition.eq(User::getId, event.getUserId());
//            UpdateWrapper<User> uw = new UpdateWrapper<>();
//            uw.set("email", null);
//            uw.eq("id", 4);
            //userMapper.update(new User(), uw);
            boolean rows = userService.update(condition);
            log.info("====绑定影响行数====" + rows);


        }

    }
}
