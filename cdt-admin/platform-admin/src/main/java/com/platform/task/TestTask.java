package com.platform.task;

import com.platform.service.SysOssService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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


    @Scheduled(cron = "0 0/2 * * * ?")
    public void test3() {
        System.out.println("111111");
    }

    public static void main(String[] args) {

        ApplicationContext context = new ClassPathXmlApplicationContext("spring-mvc.xml");
    }
}
