package chen.springframework.context.annotation;

import chen.springframework.beans.BeansException;
import chen.springframework.beans.factory.config.BeanDefinition;
import chen.springframework.beans.factory.config.BeanDefinitionHolder;
import chen.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import chen.springframework.beans.factory.support.BeanDefinitionRegistry;
import chen.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import chen.springframework.core.type.classreading.MetadataReaderFactory;
import chen.springframework.core.type.classreading.SimpleMetadataReaderFactory;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class ConfigurationClassPostProcessor implements BeanDefinitionRegistryPostProcessor {

    private MetadataReaderFactory metadataReaderFactory = new SimpleMetadataReaderFactory();

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        processConfigBeanDefinitions(registry);
    }

    public void processConfigBeanDefinitions(BeanDefinitionRegistry registry) {
        List<BeanDefinitionHolder> configCandidates = new ArrayList<>();
        String[] candidateNames = registry.getBeanDefinitionNames();

        for (String candidateName : candidateNames) {
            BeanDefinition beanDef = registry.getBeanDefinition(candidateName);
            if (ConfigurationClassUtils.checkConfigurationClassCandidate(beanDef, this.metadataReaderFactory)) {
                configCandidates.add(new BeanDefinitionHolder(beanDef, candidateName));
            }
        }

        if (configCandidates.isEmpty()) {
            return;
        }

        ConfigurationClassParser parser = new ConfigurationClassParser(this.metadataReaderFactory, registry);
        Set<BeanDefinitionHolder> candidates = new LinkedHashSet<>(configCandidates);

        parser.parse(candidates);

    }


}
