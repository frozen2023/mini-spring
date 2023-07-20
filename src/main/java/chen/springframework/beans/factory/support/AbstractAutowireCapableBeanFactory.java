package chen.springframework.beans.factory.support;

import chen.springframework.beans.BeansException;
import chen.springframework.beans.MutablePropertyValues;
import chen.springframework.beans.PropertyValue;
import chen.springframework.beans.factory.config.AutowireCapableBeanFactory;
import chen.springframework.beans.factory.config.BeanDefinition;
import chen.springframework.beans.factory.config.BeanPostProcessor;
import chen.springframework.beans.factory.config.BeanReference;
import cn.hutool.core.bean.BeanUtil;
import java.lang.reflect.Constructor;

public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory implements AutowireCapableBeanFactory {

    private InstantiationStrategy instantiationStrategy = new SimpleInstantiationStrategy();

    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeansException {
        Object bean = null;
        try {
            bean = createBeanInstance(beanName, beanDefinition, args);
            applyPropertyValues(beanName, bean, beanDefinition);
            bean = initializeBean(beanName, bean, beanDefinition);

        } catch (Exception e) {
            throw new BeansException("Instantiation of bean failed", e);
        }

        addSingleton(beanName, bean);
        return bean;
    }

    protected Object createBeanInstance(String beanName, BeanDefinition beanDefinition, Object[] args) {
        Constructor<?> constructor = null;
        Class<?> beanClass = beanDefinition.getBeanClass();
        Constructor<?>[] declaredConstructors = beanClass.getDeclaredConstructors();

        // 循环判断使用哪个构造函数
        for (Constructor<?> declaredConstructor : declaredConstructors) {
            if (args != null && args.length == declaredConstructor.getParameterTypes().length) {
                constructor = declaredConstructor;
                break;
            }
        }

        return instantiationStrategy.instantiate(beanDefinition, beanName, constructor, args);

    }

    protected void applyPropertyValues(String beanName, Object bean, BeanDefinition beanDefinition) {
        try {
            MutablePropertyValues propertyValues = beanDefinition.getPropertyValues();

            for (PropertyValue propertyValue : propertyValues.getPropertyValues()) {

                String name = propertyValue.getName();
                Object value = propertyValue.getValue();

                if (value instanceof BeanReference) {
                    BeanReference beanReference = (BeanReference) value;
                    value = getBean(beanReference.getBeanName());
                }

                BeanUtil.setFieldValue(bean, name, value);
            }
        } catch (Exception e) {
            throw new BeansException("Error setting property values：" + beanName);
        }
    }

    protected Object initializeBean(String beanName, Object bean, BeanDefinition bd) {
        Object wrappedBean = bean;

        wrappedBean = applyBeanPostProcessorsBeforeInitialization(wrappedBean, beanName);

        invokeInitMethods(beanName, wrappedBean, bd);

        wrappedBean = applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);

        return wrappedBean;
    }

    protected void invokeInitMethods(String beanName, Object bean, BeanDefinition bd) {}

    @Override
    public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) throws BeansException {
        Object result = existingBean;
        for (BeanPostProcessor processor : getBeanPostProcessors()) {
            Object current = processor.postProcessBeforeInitialization(result, beanName);
            if (current == null) {
                return result;
            }
            result = current;
        }
        return result;
    }

    @Override
    public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) throws BeansException {
        Object result = existingBean;
        for (BeanPostProcessor processor : getBeanPostProcessors()) {
            Object current = processor.postProcessAfterInitialization(result, beanName);
            if (null == current) return result;
            result = current;
        }
        return result;
    }
}
