package test.chen.springframework.context;

import chen.springframework.beans.BeansException;
import chen.springframework.beans.MutablePropertyValues;
import chen.springframework.beans.PropertyValue;
import chen.springframework.beans.factory.config.BeanDefinition;
import chen.springframework.beans.factory.config.BeanFactoryPostProcessor;
import chen.springframework.beans.factory.config.ConfigurableListableBeanFactory;

public class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        BeanDefinition beanDefinition = beanFactory.getBeanDefinition("userService");
        MutablePropertyValues propertyValues = beanDefinition.getPropertyValues();
        propertyValues.addPropertyValue(new PropertyValue("name", "改为：陈志航"));
    }
}
