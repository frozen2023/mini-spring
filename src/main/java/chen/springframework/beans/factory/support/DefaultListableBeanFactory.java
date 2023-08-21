package chen.springframework.beans.factory.support;

import chen.springframework.beans.BeansException;
import chen.springframework.beans.factory.config.BeanDefinition;
import chen.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import cn.hutool.core.lang.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory
        implements BeanDefinitionRegistry, ConfigurableListableBeanFactory {

    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);

    private volatile List<String> beanDefinitionNames = new ArrayList<>(256);

    @Override
    public BeanDefinition getBeanDefinition(String beanName) throws BeansException {
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if (beanDefinition == null) {
            throw new BeansException("No bean named '" + beanName + "' is defined");
        }
        return beanDefinition;
    }

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        this.beanDefinitionMap.put(beanName, beanDefinition);
        this.beanDefinitionNames.add(beanName);

    }

    @Override
    public int getBeanDefinitionCount() {
        return beanDefinitionMap.size();
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
        Assert.notNull(beanName, "Bean name must not be null");
        return this.beanDefinitionMap.containsKey(beanName);
    }

    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
        Map<String, T> result = new HashMap<>();
        beanDefinitionMap.forEach((beanName, beanDefinition) -> {
            Class<?> beanClass = beanDefinition.getBeanClass();
            if (type.isAssignableFrom(beanClass)) {
                result.put(beanName, (T) getBean(beanName));
            }
        });
        return result;
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return beanDefinitionMap.keySet().toArray(new String[0]);
    }

    @Override
    public String[] getBeanNamesForType(Class<?> type) {
        List<String> result = new ArrayList<>();

        for (String beanName : this.beanDefinitionNames) {
            BeanDefinition bd = getBeanDefinition(beanName);
            if (isSingleton(beanName, bd)) {
                if (isTypeMatch(beanName, type)) {
                    result.add(beanName);
                }
            }
        }

        return result.toArray(new String[0]);

    }

    private boolean isSingleton(String beanName, BeanDefinition bd) {
        return (bd != null ? bd.isSingleton() : isSingleton(beanName));
    }

    @Override
    public void preInstantiateSingletons() throws BeansException {
        for (String beanName : getBeanDefinitionNames()) {
            getBean(beanName);
        }
    }

    @Override
    public void destroySingletons() {
         super.destroySingletons();
    }

    @Override
    public <T> T getBean(Class<T> requiredType) throws BeansException {

        List<String> possibleBeanNames = new ArrayList<>();
        for (Map.Entry<String, BeanDefinition> entry : beanDefinitionMap.entrySet()) {
            Class<?> beanClass = entry.getValue().getBeanClass();
            if (requiredType.isAssignableFrom(beanClass)) {
                possibleBeanNames.add(entry.getKey());
            }
        }

        if (possibleBeanNames.size() == 1) {
            return getBean(possibleBeanNames.get(0), requiredType);
        } else {
            throw new BeansException(requiredType + "expected single bean but found " +
                    possibleBeanNames.size() + ": " + possibleBeanNames);
        }
    }

}
