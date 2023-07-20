package chen.springframework.beans.factory.config;

import chen.springframework.beans.BeansException;

public interface BeanFactoryPostProcessor {

    void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException;

}
