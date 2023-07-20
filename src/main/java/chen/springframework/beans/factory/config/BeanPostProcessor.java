package chen.springframework.beans.factory.config;

import chen.springframework.beans.BeansException;
import com.sun.istack.internal.Nullable;

public interface BeanPostProcessor {

    @Nullable
    default Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Nullable
    default Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

}
