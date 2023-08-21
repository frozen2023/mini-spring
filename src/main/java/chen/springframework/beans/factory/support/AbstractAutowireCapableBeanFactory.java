package chen.springframework.beans.factory.support;

import chen.springframework.beans.*;
import chen.springframework.beans.factory.*;
import chen.springframework.beans.factory.config.*;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.thread.threadlocal.NamedInheritableThreadLocal;
import cn.hutool.core.util.StrUtil;
import com.sun.istack.internal.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory implements AutowireCapableBeanFactory {

    private InstantiationStrategy instantiationStrategy = new SimpleInstantiationStrategy();

    private boolean allowCircularReferences = true;

    public void setInstantiationStrategy(InstantiationStrategy instantiationStrategy) {
        this.instantiationStrategy = instantiationStrategy;
    }

    public void setAllowCircularReferences(boolean allowCircularReferences) {
        this.allowCircularReferences = allowCircularReferences;
    }

    @Override
    public <T> T createBean(Class<T> beanClass) throws BeansException {
        BeanDefinition bd = new BeanDefinition(beanClass);
        bd.setScope("prototype");
        return (T) createBean(beanClass.getName(), bd, null);
    }

    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeansException {

        Object bean = resolveBeforeInstantiation(beanName, beanDefinition);
        if (bean != null) {
            return bean;
        }

        return doCreateBean(beanName, beanDefinition, args);
    }

    protected Object resolveBeforeInstantiation(String beanName, BeanDefinition bd) {
        Object bean = null;
        if (hasInstantiationAwareBeanPostProcessors()) {
            Class<?> targetType  = bd.getBeanClass();
            bean = applyBeanPostProcessorsBeforeInstantiation(targetType, beanName);
            if (bean != null) {
                applyBeanPostProcessorsAfterInitialization(bean, beanName);
            }
        }
        return bean;
    }

    protected Object applyBeanPostProcessorsBeforeInstantiation(Class<?> beanClass, String beanName) {
        for (InstantiationAwareBeanPostProcessor bpp : getBeanPostProcessorCache().instantiationAware) {
            Object result = bpp.postProcessBeforeInstantiation(beanClass, beanName);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    protected Object doCreateBean(String beanName, BeanDefinition bd, Object[] args)
            throws BeansException {

        Object bean;
        Object exposedObject;
        try {
            bean = createBeanInstance(beanName, bd, args);

            boolean earlySingletonExposure = (bd.isSingleton() && this.allowCircularReferences &&
                    isSingletonCurrentlyInCreation(beanName));
            if (earlySingletonExposure) {
                Object finalBean = bean;
                addSingletonFactory(beanName, () -> getEarlyBeanReference(beanName, bd, finalBean));
            }

            exposedObject = bean;
            populateBean(beanName, bd, bean);
            exposedObject = initializeBean(beanName, exposedObject, bd);


            if (earlySingletonExposure) {
                // 从二级缓存中获取代理对象
                Object earlySingletonReference = getSingleton(beanName, false);
                if (earlySingletonReference != null) {
                    if (exposedObject == bean) {
                        exposedObject = earlySingletonReference;
                    }
                }
            }
        } catch (Exception e) {
            throw new BeansException("Instantiation of bean failed", e);
        }

        registerDisposableBeanIfNecessary(beanName, bean, bd);

        return exposedObject;
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

    protected Object getEarlyBeanReference(String beanName, BeanDefinition bd, Object bean) {
        Object exposedObject = bean;
        if (hasInstantiationAwareBeanPostProcessors()) {
            for (SmartInstantiationAwareBeanPostProcessor bp : getBeanPostProcessorCache().smartInstantiationAware) {
                exposedObject = bp.getEarlyBeanReference(exposedObject, beanName);
            }
        }
        return exposedObject;
    }

    protected void populateBean(String beanName, BeanDefinition bd, Object bean) {
        // 是否允许属性填充
        if (hasInstantiationAwareBeanPostProcessors()) {
            for (InstantiationAwareBeanPostProcessor bp : getBeanPostProcessorCache().instantiationAware) {
                if (!bp.postProcessAfterInstantiation(bean, beanName)) {
                    return;
                }
            }
        }

        // 属性填充前操作
        PropertyValues pvs = bd.getPropertyValues();
        if (hasInstantiationAwareBeanPostProcessors()) {
            for (InstantiationAwareBeanPostProcessor bp : getBeanPostProcessorCache().instantiationAware) {
                PropertyValues pvsToUse = bp.postProcessProperties(pvs, bean, beanName);
                if (pvsToUse == null) {
                    return;
                }
                pvs = pvsToUse;
            }
        }

        // 属性填充
        if (pvs != null) {
            applyPropertyValues(beanName, bean, bd, pvs);
        }

    }

    protected void applyPropertyValues(String beanName, Object bean, BeanDefinition beanDefinition, PropertyValues pvs) {
        try {
            MutablePropertyValues propertyValues = (MutablePropertyValues) pvs;

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
        invokeAwareMethods(beanName, bean);

        Object wrappedBean = bean;

        wrappedBean = applyBeanPostProcessorsBeforeInitialization(wrappedBean, beanName);

        try {
            invokeInitMethods(beanName, wrappedBean, bd);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        wrappedBean = applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);

        return wrappedBean;
    }

    private void invokeAwareMethods(String beanName, Object bean) {
        if (bean instanceof Aware) {
            if (bean instanceof BeanNameAware) {
                ((BeanNameAware) bean).setBeanName(beanName);
            }
            if (bean instanceof BeanClassLoaderAware) {
                ClassLoader bcl = getBeanClassLoader();
                if (bcl != null) {
                    ((BeanClassLoaderAware) bean).setBeanClassLoader(bcl);
                }
            }
            if (bean instanceof BeanFactoryAware) {
                ((BeanFactoryAware) bean).setBeanFactory(AbstractAutowireCapableBeanFactory.this);
            }
        }
    }

    protected void invokeInitMethods(String beanName, Object bean, BeanDefinition bd) throws Exception {

        boolean isInitializingBean = (bean instanceof InitializingBean);

        if (isInitializingBean) {
            ((InitializingBean) bean).afterPropertiesSet();
        }

        if (bd != null ) {
            String initMethodName = bd.getInitMethodName();
            if (StrUtil.isNotEmpty(initMethodName) && !(isInitializingBean && "afterPropertiesSet".equals(initMethodName))) {
                Class<?> beanClass = bd.getBeanClass();
                Method method = beanClass.getMethod(initMethodName);
                method.invoke(bean);
            }
        }

    }

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
