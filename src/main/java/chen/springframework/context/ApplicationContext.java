package chen.springframework.context;

import chen.springframework.beans.factory.ListableBeanFactory;
import chen.springframework.beans.factory.config.AutowireCapableBeanFactory;

public interface ApplicationContext extends ListableBeanFactory, ApplicationEventPublisher {

    AutowireCapableBeanFactory getAutowireCapableBeanFactory() throws IllegalStateException;

}
