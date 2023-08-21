package test.chen.springframework.context;

import chen.springframework.context.ApplicationListener;

import java.util.Date;

public class CustomEventListener implements ApplicationListener<CustomEvent> {

    @Override
    public void onApplicationEvent(CustomEvent event) {
        System.out.println("收到来自" + event.getSource() + "的消息，时间：" + new Date());
        System.out.println("成功消费！id:" + event.getId() + "  info:" + event.getInfo());
    }
}
