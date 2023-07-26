package chen.springframework.context.event;

import chen.springframework.beans.BeansException;
import chen.springframework.beans.factory.BeanClassLoaderAware;
import chen.springframework.beans.factory.BeanFactory;
import chen.springframework.beans.factory.BeanFactoryAware;
import chen.springframework.beans.factory.config.ConfigurableBeanFactory;
import chen.springframework.context.ApplicationEvent;
import chen.springframework.context.ApplicationListener;
import com.sun.istack.internal.Nullable;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

public abstract class AbstractApplicationEventMulticaster
        implements ApplicationEventMulticaster, BeanClassLoaderAware, BeanFactoryAware {

    public final Set<ApplicationListener<?>> applicationListeners = new LinkedHashSet<>();

    @Nullable
    private ClassLoader beanClassLoader;

    @Nullable
    private ConfigurableBeanFactory beanFactory;

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.beanClassLoader = classLoader;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        if (!(beanFactory instanceof ConfigurableBeanFactory)) {
            throw new IllegalStateException("Not running in a ConfigurableBeanFactory: " + beanFactory);
        }
        this.beanFactory = (ConfigurableBeanFactory) beanFactory;
    }

    @Override
    public void addApplicationListener(ApplicationListener<?> listener) {
        applicationListeners.add(listener);
    }

    @Override
    public void removeApplicationListener(ApplicationListener<?> listener) {
        applicationListeners.remove(listener);
    }

    protected Collection<ApplicationListener<?>> getApplicationListeners(ApplicationEvent event) {
        LinkedList<ApplicationListener<?>> allListeners = new LinkedList<>();
        for (ApplicationListener<?> listener : applicationListeners) {
            if (supportsEvent(listener, event)) {
                allListeners.add(listener);
            }
        }
        return allListeners;
    }

    // 获取泛型类型判断是否接收该事件
    protected boolean supportsEvent(ApplicationListener<?> listener, ApplicationEvent event) {

        Class<?> targetClass = listener.getClass();
        Type genericInterface = targetClass.getGenericInterfaces()[0];

        Type actualTypeArgument = ((ParameterizedType) genericInterface).getActualTypeArguments()[0];
        String className = actualTypeArgument.getTypeName();
        Class<?> eventClass;
        try {
            eventClass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new BeansException("wrong event class name: " + className);
        }
        return eventClass.isAssignableFrom(event.getClass());
    }
}
