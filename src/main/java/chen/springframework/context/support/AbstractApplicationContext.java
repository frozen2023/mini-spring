package chen.springframework.context.support;

import chen.springframework.beans.BeansException;
import chen.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import chen.springframework.context.ApplicationEvent;
import chen.springframework.context.ApplicationListener;
import chen.springframework.context.ConfigurableApplicationContext;
import chen.springframework.context.event.ApplicationContextEvent;
import chen.springframework.context.event.ApplicationEventMulticaster;
import chen.springframework.context.event.ContextClosedEvent;
import chen.springframework.context.event.SimpleApplicationEventMulticaster;
import chen.springframework.core.io.DefaultResourceLoader;
import cn.hutool.core.lang.Assert;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public abstract class AbstractApplicationContext extends DefaultResourceLoader implements ConfigurableApplicationContext {

    public static final String APPLICATION_EVENT_MULTICASTER_BEAN_NAME = "applicationEventMulticaster";

    private ApplicationEventMulticaster applicationEventMulticaster;

    private final Set<ApplicationListener<?>> applicationListeners = new LinkedHashSet<>();

    @Override
    public void refresh() throws BeansException {

        ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();

        prepareBeanFactory(beanFactory);

        invokeBeanFactoryPostProcessors(beanFactory);

        registerBeanPostProcessors(beanFactory);

        initApplicationEventMulticaster();

        registerListeners();

        finishBeanFactoryInitialization(beanFactory);

    }

    @Override
    public void registerShutdownHook() {
        Thread shutdownHook = new Thread(this::close);
        Runtime.getRuntime().addShutdownHook(shutdownHook);
    }

    @Override
    public void close() {
        publishEvent(new ContextClosedEvent(this));
        getBeanFactory().destroySingletons();
    }

    protected ConfigurableListableBeanFactory obtainFreshBeanFactory() {
        refreshBeanFactory();
        return getBeanFactory();
    }

    protected void invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory) {
        PostProcessorRegistrationDelegate.invokeBeanFactoryPostProcessors(beanFactory);
    }

    protected void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory) {
        PostProcessorRegistrationDelegate.registerBeanPostProcessors(beanFactory);
    }

    protected void finishBeanFactoryInitialization(ConfigurableListableBeanFactory beanFactory) {
        beanFactory.preInstantiateSingletons();
    }

    protected abstract void refreshBeanFactory() throws BeansException;

    protected abstract ConfigurableListableBeanFactory getBeanFactory();

    protected void prepareBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));
    }

    protected void initApplicationEventMulticaster() {
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();
        this.applicationEventMulticaster = new SimpleApplicationEventMulticaster(beanFactory);
        beanFactory.registerSingleton(APPLICATION_EVENT_MULTICASTER_BEAN_NAME, this.applicationEventMulticaster);
    }

    protected void registerListeners() {

        for (ApplicationListener<?> listener : getApplicationListeners()) {
            getApplicationEventMulticaster().addApplicationListener(listener);
        }

        Collection<ApplicationListener> applicationListeners = getBeansOfType(ApplicationListener.class).values();
        for (ApplicationListener listener : applicationListeners) {
            getApplicationEventMulticaster().addApplicationListener(listener);
        }
    }

    protected void finishRefresh() {
        publishEvent(new ApplicationContextEvent(this));
    }

    public Set<ApplicationListener<?>> getApplicationListeners() {
        return applicationListeners;
    }

    public ApplicationEventMulticaster getApplicationEventMulticaster() {
        return applicationEventMulticaster;
    }

    @Override
    public void addApplicationListener(ApplicationListener<?> listener) {
        Assert.notNull(listener, "ApplicationListener must not be null");
        if (this.applicationEventMulticaster != null) {
            this.applicationEventMulticaster.addApplicationListener(listener);
        }
        this.applicationListeners.add(listener);
    }

    @Override
    public void removeApplicationListener(ApplicationListener<?> listener) {
        Assert.notNull(listener, "ApplicationListener must not be null");
        if (this.applicationEventMulticaster != null) {
            this.applicationEventMulticaster.removeApplicationListener(listener);
        }
        this.applicationListeners.remove(listener);
    }

    @Override
    public void publishEvent(ApplicationEvent event) {
        getApplicationEventMulticaster().multicastEvent(event);
    }

    @Override
    public Object getBean(String name) throws BeansException {
        return getBeanFactory().getBean(name);
    }

    @Override
    public Object getBean(String name, Object... args) throws BeansException {
        return getBeanFactory().getBean(name, args);
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return getBeanFactory().containsBeanDefinition(beanName);
    }

    @Override
    public int getBeanDefinitionCount() {
        return getBeanFactory().getBeanDefinitionCount();
    }

    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
        return getBeanFactory().getBeansOfType(type);
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return getBeanFactory().getBeanDefinitionNames();
    }
}
