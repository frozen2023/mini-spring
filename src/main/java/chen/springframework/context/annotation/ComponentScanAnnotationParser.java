package chen.springframework.context.annotation;

import chen.springframework.beans.factory.config.BeanDefinitionHolder;
import chen.springframework.beans.factory.support.BeanDefinitionRegistry;
import java.util.Set;

public class ComponentScanAnnotationParser {

    private final BeanDefinitionRegistry registry;

    public ComponentScanAnnotationParser(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    public Set<BeanDefinitionHolder> parse(AnnotationAttributes componentScan, String declaringClass) {
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(this.registry);
        String[] basePackages = componentScan.getStringArray("basePackages");
        return scanner.doScan(basePackages);
    }
}
