package chen.springframework.context;

import chen.springframework.context.event.ContextClosedEvent;

import java.util.Date;

public class ContextClosedEventListener implements ApplicationListener<ContextClosedEvent> {
    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        System.out.println("关闭事件：" + event + "时间：" + new Date());
    }
}
