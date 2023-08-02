package chen.springframework.aop.framework.autoproxy;

import chen.springframework.aop.Advisor;
import chen.springframework.aop.Pointcut;
import chen.springframework.aop.TargetSource;
import chen.springframework.aop.framework.ProxyFactory;
import chen.springframework.aop.framework.ProxyProcessorSupport;
import chen.springframework.aop.target.SingletonTargetSource;
import chen.springframework.beans.BeansException;
import chen.springframework.beans.PropertyValues;
import chen.springframework.beans.factory.BeanFactory;
import chen.springframework.beans.factory.BeanFactoryAware;
import chen.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;
import com.sun.istack.internal.Nullable;
import org.aopalliance.aop.Advice;

import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractAutoProxyCreator extends ProxyProcessorSupport
        implements SmartInstantiationAwareBeanPostProcessor, BeanFactoryAware {

    private BeanFactory beanFactory;

    private final Map<Object, Object> earlyProxyReferences = new ConcurrentHashMap<>(16);

    private final Map<Object, Boolean> advisedBeans = new ConcurrentHashMap<>(256);

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    protected BeanFactory getBeanFactory() {
        return this.beanFactory;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean != null) {
            if (this.earlyProxyReferences.remove(beanName) != bean) {
                return wrapIfNecessary(bean, beanName);
            }
        }
        return bean;
    }

    @Override
    public Object getEarlyBeanReference(Object bean, String beanName) throws BeansException {
        this.earlyProxyReferences.put(beanName, bean);
        return wrapIfNecessary(bean, beanName);
    }

    protected Object wrapIfNecessary(Object bean, String beanName) {

        if(Boolean.FALSE.equals(this.advisedBeans.get(beanName))) {
            return bean;
        }

        if (isInfrastructureClass(bean.getClass())) {
            this.advisedBeans.put(beanName, Boolean.FALSE);
            return bean;
        }

        List<Advisor> specificInterceptors = getAdvicesAndAdvisorsForBean(bean.getClass(), beanName, null);
        if (specificInterceptors != null) {
            this.advisedBeans.put(beanName, Boolean.TRUE);
            Object proxy = createProxy(bean.getClass(), beanName, specificInterceptors, new SingletonTargetSource(bean));
            return proxy;
        }

        this.advisedBeans.put(beanName, Boolean.FALSE);
        return bean;
    }

    protected boolean isInfrastructureClass(Class<?> beanClass) {
        return Advice.class.isAssignableFrom(beanClass)
                || Pointcut.class.isAssignableFrom(beanClass)
                || Advisor.class.isAssignableFrom(beanClass);
    }

    protected Object createProxy(Class<?> beanClass, String beanName,
            List<Advisor> specificInterceptors, TargetSource targetSource) {
        return buildProxy(beanClass, beanName, specificInterceptors, targetSource);
    }

    private Object buildProxy(Class<?> beanClass, @Nullable String beanName,
                              @Nullable List<Advisor> specificInterceptors, TargetSource targetSource) {
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.copyFrom(this);

        if (proxyFactory.isProxyTargetClass()) {
            if (Proxy.isProxyClass(beanClass)) {
                for (Class<?> ifc : beanClass.getInterfaces()) {
                    proxyFactory.addInterface(ifc);
                }
            }
        } else {
            if (shouldProxyTargetClass(beanClass, beanName)) {
                proxyFactory.setProxyTargetClass(true);
            } else {
                evaluateProxyInterfaces(beanClass, proxyFactory);
            }
        }

        proxyFactory.addAdvisors(specificInterceptors);
        proxyFactory.setTargetSource(targetSource);

        return proxyFactory.getProxy();
    }

    // 从xml中读取
    protected boolean shouldProxyTargetClass(Class<?> beanClass, @Nullable String beanName) {
        return false;
    }

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        return null;
    }

    @Override
    public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        return pvs;
    }

    protected abstract List<Advisor> getAdvicesAndAdvisorsForBean(Class<?> beanClass, String beanName,
                                                                  @Nullable TargetSource customTargetSource) throws BeansException;
}

