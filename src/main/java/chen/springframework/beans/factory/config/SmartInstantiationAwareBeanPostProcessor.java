package chen.springframework.beans.factory.config;

import chen.springframework.beans.BeansException;

public interface SmartInstantiationAwareBeanPostProcessor extends InstantiationAwareBeanPostProcessor {

    default Object getEarlyBeanReference(Object bean, String beanName) throws BeansException {
        return bean;
    }

}
