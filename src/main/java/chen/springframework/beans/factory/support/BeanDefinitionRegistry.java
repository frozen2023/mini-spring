package chen.springframework.beans.factory.support;

import chen.springframework.beans.BeansException;
import chen.springframework.beans.factory.config.BeanDefinition;

public interface BeanDefinitionRegistry {

    BeanDefinition getBeanDefinition(String beanName) throws BeansException;

    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);

    int getBeanDefinitionCount();

    boolean containsBeanDefinition(String beanName);

    String[] getBeanDefinitionNames();
}
