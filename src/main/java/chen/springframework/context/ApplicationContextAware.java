package chen.springframework.context;

import chen.springframework.beans.BeansException;
import chen.springframework.beans.factory.Aware;

public interface ApplicationContextAware extends Aware {

    void setApplicationContext(ApplicationContext applicationContext) throws BeansException;

}
