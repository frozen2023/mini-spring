package chen.springframework.beans.factory;

import chen.springframework.beans.BeansException;

public interface BeanFactory {

    Object getBean(String name) throws BeansException;

    Object getBean(String name, Object... args) throws BeansException;

    <T> T getBean(String name, Class<T> requiredType) throws BeansException;

    <T> T getBean(Class<T> requiredType) throws BeansException;

    boolean isSingleton(String name);

    boolean isTypeMatch(String name, Class<?> typeToMatch);
}
