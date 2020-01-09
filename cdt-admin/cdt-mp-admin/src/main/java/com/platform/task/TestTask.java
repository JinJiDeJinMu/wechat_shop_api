package com.platform.task;

import com.platform.service.SysOssService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 测试定时任务(演示Demo，可删除)
 * <p>
 * testTask为spring bean的名称
 *
 * @date 2016年11月30日 下午1:34:24
 */
@Component
public class TestTask {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SysOssService sysOssService;

    public void test(String params) {
        logger.info("我是带参数的test方法，正在被执行，参数为：" + params);
    }

    public void test2() {
        logger.info("我是不带参数的test2方法，正在被执行");
    }

    public static void main(String[] args) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime now1 = now.plusMinutes(30L);
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println(now);
        System.out.println(df.format(now));
    }

}