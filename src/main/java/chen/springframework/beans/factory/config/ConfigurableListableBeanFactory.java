package chen.springframework.beans.factory.config;

import chen.springframework.beans.BeansException;
import chen.springframework.beans.factory.ListableBeanFactory;

public interface ConfigurableListableBeanFactory
        extends ListableBeanFactory, AutowireCapableBeanFactory, ConfigurableBeanFactory {

    BeanDefinition getBeanDefinition(String beanName) throws BeansException;

    void preInstantiateSingletons() throws BeansException;

}
