package chen.springframework.context.support;

import chen.springframework.beans.factory.config.BeanFactoryPostProcessor;
import chen.springframework.beans.factory.config.BeanPostProcessor;
import chen.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import java.util.Map;

public class PostProcessorRegistrationDelegate {

    public PostProcessorRegistrationDelegate() {
    }

    public static void invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory) {
        Map<String, BeanFactoryPostProcessor> map = beanFactory.getBeansOfType(BeanFactoryPostProcessor.class);
        for (BeanFactoryPostProcessor postProcessor : map.values()) {
            postProcessor.postProcessBeanFactory(beanFactory);
        }
    }

    public static void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory) {
        Map<String, BeanPostProcessor> map = beanFactory.getBeansOfType(BeanPostProcessor.class);
        for (BeanPostProcessor beanPostProcessor : map.values()) {
            beanFactory.addBeanPostProcessor(beanPostProcessor);
        }
    }
}
