package chen.springframework.beans.factory.support;

import chen.springframework.beans.BeansException;
import chen.springframework.beans.factory.BeanFactory;
import chen.springframework.beans.factory.config.BeanDefinition;
import chen.springframework.beans.factory.config.BeanPostProcessor;
import chen.springframework.beans.factory.config.ConfigurableBeanFactory;
import cn.hutool.core.lang.Assert;
import com.sun.istack.internal.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements ConfigurableBeanFactory {

    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    @Override
    public Object getBean(String name) throws BeansException {
        return doGetBean(name, null);
    }

    @Override
    public Object getBean(String name, Object... args) throws BeansException {
        return doGetBean(name, args);
    }

    protected Object doGetBean(String name, Object[] args) {
        Object bean = getSingleton(name);
        if (bean != null) {
            return bean;
        }
        BeanDefinition beanDefinition = getBeanDefinition(name);
        return createBean(name, beanDefinition, args);
    }

    @Override
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        Assert.notNull(beanPostProcessor, "BeanPostProcessor must not be null");
        synchronized (this.beanPostProcessors) {
            // Remove from old position, if any
            this.beanPostProcessors.remove(beanPostProcessor);
            // Add to end of list
            this.beanPostProcessors.add(beanPostProcessor);
        }
    }

    public List<BeanPostProcessor> getBeanPostProcessors() {
        return beanPostProcessors;
    }

    protected abstract BeanDefinition getBeanDefinition(String beanName) throws BeansException;

    protected abstract Object createBean(String beanName, BeanDefinition beanDefinition, @Nullable Object[] args) throws BeansException;
}
