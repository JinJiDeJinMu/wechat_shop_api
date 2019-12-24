package com.chundengtai.base.event;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executors;

@Component
public class EventBusCenter {
    //    @Resource(name ="taskExecutorCdt")
//    AsyncTaskExecutor executor;
    // 管理同步事件
    private EventBus syncEventBus = new EventBus();

    // 管理异步事件
    private AsyncEventBus asyncEventBus = new AsyncEventBus(Executors.newCachedThreadPool());

    public void postSync(Object event) {
        syncEventBus.post(event);
    }

    public void postAsync(Object event) {
        asyncEventBus.post(event);
    }

    @PostConstruct
    public void init() {

        // 获取所有带有 @EventBusListener 的 bean，将他们注册为监听者
//        List<Object> listeners = SpringContextUtils.getBeansWithAnnotation(EventBusListener.class);
//        for (Object listener : listeners) {
//            asyncEventBus.register(listener);
//            syncEventBus.register(listener);
//        }
    }
}
