package chen.springframework.context.event;

import chen.springframework.beans.factory.BeanFactory;
import chen.springframework.context.ApplicationEvent;
import chen.springframework.context.ApplicationListener;

public class SimpleApplicationEventMulticaster extends AbstractApplicationEventMulticaster {

    public SimpleApplicationEventMulticaster() {
    }

    public SimpleApplicationEventMulticaster(BeanFactory beanFactory) {
        setBeanFactory(beanFactory);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public void multicastEvent(ApplicationEvent event) {
        for (ApplicationListener applicationListener : getApplicationListeners(event)) {
            applicationListener.onApplicationEvent(event);
        }
    }
}
