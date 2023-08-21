package chen.springframework.beans.factory;

import chen.springframework.beans.BeansException;
import com.sun.istack.internal.Nullable;

import java.util.Map;

public interface ListableBeanFactory extends BeanFactory {

    boolean containsBeanDefinition(String beanName);

    int getBeanDefinitionCount();

    <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException;

    String[] getBeanDefinitionNames();

    String[] getBeanNamesForType(@Nullable Class<?> type);

}
