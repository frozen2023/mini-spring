package chen.springframework.beans.factory.support;

import chen.springframework.beans.BeansException;
import chen.springframework.beans.factory.DisposableBean;
import chen.springframework.beans.factory.FactoryBean;
import chen.springframework.beans.factory.config.*;
import chen.springframework.util.ClassUtils;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.sun.istack.internal.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class AbstractBeanFactory extends FactoryBeanRegistrySupport implements ConfigurableBeanFactory {

    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    private BeanPostProcessorCache beanPostProcessorCache;

    private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();

    public ClassLoader getBeanClassLoader() {
        return beanClassLoader;
    }

    public void setBeanClassLoader(ClassLoader beanClassLoader) {
        this.beanClassLoader = beanClassLoader;
    }

    @Override
    public Object getBean(String name) throws BeansException {
        return doGetBean(name, null);
    }

    @Override
    public Object getBean(String name, Object... args) throws BeansException {
        return doGetBean(name, args);
    }

    protected Object doGetBean(String name, Object[] args) {
        Object beanInstance;
        Object sharedInstance = getSingleton(name);
        if (sharedInstance != null) {
            return getObjectForBeanInstance(sharedInstance, name);
        }

        BeanDefinition beanDefinition = getBeanDefinition(name);
        if (beanDefinition.isSingleton()) {
            sharedInstance = getSingleton(name, () -> createBean(name, beanDefinition, args));
            beanInstance = getObjectForBeanInstance(sharedInstance, name);
        } else {
            Object prototypeInstance = createBean(name, beanDefinition, args);
            beanInstance = getObjectForBeanInstance(prototypeInstance, name);
        }

        return beanInstance;
    }

    protected Object getObjectForBeanInstance(Object beanInstance, String name) {
        if (!(beanInstance instanceof FactoryBean)) {
            return beanInstance;
        }

        Object object = getCachedObjectForFactoryBean(name);

        if (object == null) {
            FactoryBean<?> factoryBean = (FactoryBean<?>) beanInstance;
            object = getObjectFromFactoryBean(factoryBean, name);
        }

        return object;
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
        resetBeanPostProcessorCache();
    }

    private void resetBeanPostProcessorCache() {
        synchronized (this.beanPostProcessors) {
            this.beanPostProcessorCache = null;
        }
    }

    public List<BeanPostProcessor> getBeanPostProcessors() {
        return beanPostProcessors;
    }

    BeanPostProcessorCache getBeanPostProcessorCache() {
        synchronized (this.beanPostProcessors) {
            BeanPostProcessorCache bppCache = this.beanPostProcessorCache;
            if (bppCache == null) {
                bppCache = new BeanPostProcessorCache();
                for (BeanPostProcessor bpp : this.beanPostProcessors) {
                    if (bpp instanceof InstantiationAwareBeanPostProcessor) {
                        bppCache.instantiationAware.add((InstantiationAwareBeanPostProcessor) bpp);
                        if (bpp instanceof SmartInstantiationAwareBeanPostProcessor) {
                            bppCache.smartInstantiationAware.add((SmartInstantiationAwareBeanPostProcessor) bpp);
                        }
                    }
                }
                this.beanPostProcessorCache = bppCache;
            }
            return bppCache;
        }
    }

    protected boolean hasInstantiationAwareBeanPostProcessors() {
        return !getBeanPostProcessorCache().instantiationAware.isEmpty();
    }

    protected abstract BeanDefinition getBeanDefinition(String beanName) throws BeansException;

    protected abstract Object createBean(String beanName, BeanDefinition beanDefinition, @Nullable Object[] args) throws BeansException;

    protected void registerDisposableBeanIfNecessary(String beanName, Object bean, BeanDefinition beanDefinition) {

        if (!beanDefinition.isPrototype()) {
            if (beanDefinition.isSingleton()) {
                if (bean instanceof DisposableBean || StrUtil.isNotEmpty(beanDefinition.getDestroyMethodName())) {
                    registerDisposableBean(beanName, new DisposableBeanAdapter(bean, beanName, beanDefinition));
                }
            }
        }
    }

    static class BeanPostProcessorCache {

        final List<InstantiationAwareBeanPostProcessor> instantiationAware = new ArrayList<>();

        final List<SmartInstantiationAwareBeanPostProcessor> smartInstantiationAware = new ArrayList<>();

    }

}
