package chen.springframework.beans.factory.support;

import chen.springframework.beans.BeansException;
import chen.springframework.beans.factory.config.BeanFactoryPostProcessor;

public interface BeanDefinitionRegistryPostProcessor extends BeanFactoryPostProcessor {

    void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException;

}
