package chen.springframework.context.support;

import chen.springframework.beans.factory.config.BeanFactoryPostProcessor;
import chen.springframework.beans.factory.config.BeanPostProcessor;
import chen.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import chen.springframework.beans.factory.support.BeanDefinitionRegistry;
import chen.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.w3c.dom.ls.LSInput;

import java.util.*;

public class PostProcessorRegistrationDelegate {

    public PostProcessorRegistrationDelegate() {
    }

    public static void invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory) {

        Set<String> processedBeans = new HashSet<>();

        if (beanFactory instanceof BeanDefinitionRegistry) {
            String[] postProcessorNames;
            List<BeanDefinitionRegistryPostProcessor> registryProcessors = new ArrayList<>();
            List<BeanDefinitionRegistryPostProcessor> currentRegistryProcessors = new ArrayList<>();
            boolean reiterate = true;
            while (reiterate) {
                reiterate = false;
                postProcessorNames = beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class);
                for (String ppName : postProcessorNames) {
                    if (!processedBeans.contains(ppName)) {
                        currentRegistryProcessors.add(beanFactory.getBean(ppName, BeanDefinitionRegistryPostProcessor.class));
                        processedBeans.add(ppName);
                        reiterate = true;
                    }
                }
                registryProcessors.addAll(currentRegistryProcessors);
                invokeBeanDefinitionRegistryPostProcessors(currentRegistryProcessors, (BeanDefinitionRegistry) beanFactory);
                currentRegistryProcessors.clear();
            }
            invokeBeanFactoryPostProcessors(registryProcessors, beanFactory);
        }

        String[] postProcessorNames = beanFactory.getBeanNamesForType(BeanFactoryPostProcessor.class);
        List<BeanFactoryPostProcessor> beanFactoryPostProcessors = new ArrayList<>();
        for (String ppName : postProcessorNames) {
            beanFactoryPostProcessors.add(beanFactory.getBean(ppName, BeanFactoryPostProcessor.class));
        }
        invokeBeanFactoryPostProcessors(beanFactoryPostProcessors, beanFactory);
    }

    public static void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory) {
        Map<String, BeanPostProcessor> map = beanFactory.getBeansOfType(BeanPostProcessor.class);
        for (BeanPostProcessor beanPostProcessor : map.values()) {
            beanFactory.addBeanPostProcessor(beanPostProcessor);
        }
    }

    private static void invokeBeanDefinitionRegistryPostProcessors(
            Collection<? extends BeanDefinitionRegistryPostProcessor> postProcessors, BeanDefinitionRegistry registry) {
        for (BeanDefinitionRegistryPostProcessor postProcessor : postProcessors) {
            postProcessor.postProcessBeanDefinitionRegistry(registry);
        }
    }

    private static void invokeBeanFactoryPostProcessors(
            Collection<? extends BeanFactoryPostProcessor> postProcessors, ConfigurableListableBeanFactory beanFactory) {
        for (BeanFactoryPostProcessor postProcessor : postProcessors) {
            postProcessor.postProcessBeanFactory(beanFactory);
        }
    }
}
