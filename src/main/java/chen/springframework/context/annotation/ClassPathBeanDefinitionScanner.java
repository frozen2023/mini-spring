package chen.springframework.context.annotation;

import chen.springframework.beans.factory.config.BeanDefinition;
import chen.springframework.beans.factory.config.BeanDefinitionHolder;
import chen.springframework.beans.factory.support.BeanDefinitionRegistry;
import chen.springframework.stereotype.Component;
import cn.hutool.core.util.StrUtil;

import java.util.LinkedHashSet;
import java.util.Set;

public class ClassPathBeanDefinitionScanner extends ClassPathScanningCandidateComponentProvider {

    private final BeanDefinitionRegistry registry;

    public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitions = new LinkedHashSet<>();
        for (String basePackage : basePackages) {
            Set<BeanDefinition> candidates = findCandidateComponents(basePackage);
            for (BeanDefinition candidate : candidates) {
                String scope = resolveBeanScope(candidate);
                if (StrUtil.isNotEmpty(scope)) {
                    candidate.setScope(scope);
                }

                String beanName = generateBeanName(candidate);

                registry.registerBeanDefinition(beanName, candidate);
                beanDefinitions.add(new BeanDefinitionHolder(candidate, beanName));
            }
        }
        return beanDefinitions;

    }

    private String resolveBeanScope(BeanDefinition beanDefinition) {
        Class<?> beanClass = beanDefinition.getBeanClass();
        Scope scope = beanClass.getAnnotation(Scope.class);
        if (scope != null) {
            return scope.value();
        }
        return "";
    }

    private String generateBeanName(BeanDefinition beanDefinition) {
        Class<?> beanClass = beanDefinition.getBeanClass();
        Component component = beanClass.getAnnotation(Component.class);
        String value = component.value();
        if (StrUtil.isEmpty(value)) {
            value = StrUtil.lowerFirst(beanClass.getSimpleName());
        }
        return value;
    }

}
