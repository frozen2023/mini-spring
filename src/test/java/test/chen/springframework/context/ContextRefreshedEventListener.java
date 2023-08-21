package test.chen.springframework.context;

import chen.springframework.context.ApplicationListener;
import chen.springframework.context.event.ContextRefreshedEvent;

import java.util.Date;

public class ContextRefreshedEventListener implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        System.out.println("刷新事件：" + event + "时间：" + new Date());
    }
}
