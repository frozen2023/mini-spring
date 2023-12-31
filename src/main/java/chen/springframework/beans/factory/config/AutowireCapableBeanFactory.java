package chen.springframework.beans.factory.config;

import chen.springframework.beans.BeansException;
import chen.springframework.beans.factory.BeanFactory;

public interface AutowireCapableBeanFactory extends BeanFactory {

    <T> T createBean(Class<T> beanClass) throws BeansException;

    Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName)
            throws BeansException;

    Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName)
            throws BeansException;

}
